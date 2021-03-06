package com.chandigarhadmin.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.adapter.ChatAdapter;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.BranchesModel;
import com.chandigarhadmin.models.ChatPojoModel;
import com.chandigarhadmin.models.CreateTicketResponse;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.service.ApiServiceTask;
import com.chandigarhadmin.service.JSONParser;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chandigarhadmin.models.RequestParams.TYPE_CREATE_TICKET;
import static com.chandigarhadmin.models.RequestParams.TYPE_GET_ALL_TICKET;
import static com.chandigarhadmin.models.RequestParams.TYPE_GET_BRANCHES;
import static com.chandigarhadmin.service.JSONParser.GET;

public class AdminAgentActivity extends Activity implements PopupMenu.OnMenuItemClickListener, AIListener, ResponseCallback, SelectionCallbacks {
    //Create placeholder for user's consent to record_audio permission.
    //This will be used in handling callback
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.querystringet)
    EditText etInputBox;
    @BindView(R.id.btn_chat_search)
    ImageView sendicon;
    @BindView(R.id.keyboardicon)
    ImageView keyboardicon;
    @BindView(R.id.micicon)
    ImageView micicon;
    @BindView(R.id.recognition_view)
    RecognitionProgressView recognitionProgressView;
    private AIService aiService;
    private SpeechRecognizer speechRecognizer;
    private AIConfiguration aiConfiguration;
    private ArrayList<ChatPojoModel> chatBotResponseList;
    private SessionManager sessionManager;
    private ChatAdapter mAdapter;
    private TextToSpeech textToSpeech;
    private CreateTicketResponse createTicketResponse;

    @OnClick(R.id.btn_chat_search)
    public void sendClick() {
        if (!etInputBox.getText().toString().equalsIgnoreCase("")) {
            String input = etInputBox.getText().toString();
            setChatInputs(input, true);
            etInputBox.setText("");
            sendRequest(input);
        }
    }

    @OnClick(R.id.keyboardicon)
    public void keyboardClick() {
        keyboardicon.setVisibility(View.GONE);
        micicon.setVisibility(View.VISIBLE);
        recognitionProgressView.setVisibility(View.GONE);
        etInputBox.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.micicon)
    public void micClick() {
        keyboardicon.setVisibility(View.VISIBLE);
        sendicon.setVisibility(View.GONE);
        recognitionProgressView.setVisibility(View.VISIBLE);
        micicon.setVisibility(View.GONE);
        etInputBox.setVisibility(View.GONE);
        startRecognition();
        recognitionProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRecognition();
            }
        }, 50);
    }

    @OnClick(R.id.recognition_view)
    public void recognitionClick() {
        startRecognition();
        recognitionProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRecognition();
            }
        }, 50);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        ButterKnife.bind(this);
        requestAudioPermissions();
        sessionManager = new SessionManager(this);
        initializeAI();
        initializeViews();
        setChatInputs("Hi, How may i Assist you ?", false);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                startActivity(new Intent(this, MyAccountActivity.class));
                return true;
            case R.id.view_tickets:
                return true;
            case R.id.settings:
            case R.id.what_you_do:
            case R.id.help:
            case R.id.send_feedback:
                Constant.showToastMessage(AdminAgentActivity.this, getResources().getString(R.string.out_of_scope));
                return true;
            case R.id.logout_menu:
                sessionManager.clearAllData();
                Intent intent = new Intent(AdminAgentActivity.this, LanguageSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

    //Method to initialize AI
    private void initializeAI() {
        aiConfiguration = new AIConfiguration(Constant.AI_CONFIGURATION_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, aiConfiguration);
        aiService.setListener(this);
    }

    //Method to initialize recyclerview
    private void initializeViews() {
        chatBotResponseList = new ArrayList<>();
        mAdapter = new ChatAdapter(this, chatBotResponseList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        etInputBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (!etInputBox.getText().toString().equalsIgnoreCase("")) {
                        String input = etInputBox.getText().toString();
                        setChatInputs(input, true);
                        etInputBox.setText("");
                        sendRequest(input);
                    }
                    return true;
                }
                return false;
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(AdminAgentActivity.this);

        int[] colors = {
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                ContextCompat.getColor(this, R.color.colorPrimary)
        };
        recognitionProgressView.setSpeechRecognizer(speechRecognizer);
        recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });
        recognitionProgressView.setColors(colors);
        int[] heights = {20, 24, 18, 23, 16};
        recognitionProgressView.setBarMaxHeightsInDp(heights);
        recognitionProgressView.play();

        //making code to autoscroll when layout changes
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(
                                recyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 1);
            }
        });
    }

    //AIRequest should have query OR event
    private void sendRequest(String queryString) {
        createTicketResponse = null;
        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {
            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];
                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                try {
                    return aiService.textRequest(request);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };
        task.execute(queryString);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        if (Constant.isNetworkAvailable(AdminAgentActivity.this)) {
            if (result.getAction().equalsIgnoreCase("createticket")) {

                if (result.getParameters().get("department").toString().equalsIgnoreCase("[]")) {
                    new ApiServiceTask(AdminAgentActivity.this, this, TYPE_GET_BRANCHES).execute(Constant.BASE + "branches");
                } else if (result.getParameters().get("ticketsubject").toString().equalsIgnoreCase("[]")) {
                    setChatInputs(response.getResult().getFulfillment().getSpeech(), false);
                } else if (result.getParameters().get("ticketdesc").toString().equalsIgnoreCase("[]")) {
                    setChatInputs(response.getResult().getFulfillment().getSpeech(), false);
                } else if (result.getFulfillment().getSpeech().equalsIgnoreCase("save ticket")) {
                    setChatInputs("Creating ticket...", false);
                    createTicket(result);
                    Log.e("result", "Saved");
                }
            } else if (result.getAction().equalsIgnoreCase("fetchalltickets")) {
                getTickets();
            } else {
                setChatInputs(response.getResult().getFulfillment().getSpeech(), false);
            }
        } else {
            Constant.showToastMessage(AdminAgentActivity.this, getString(R.string.no_internet));
        }
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onResponse(String result, String type) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        if (!result.contains("error") && !result.equalsIgnoreCase("Failed")) {

            if (type.equalsIgnoreCase(TYPE_GET_BRANCHES)) {
                setChatInputs("Okay!! Please select a department for which you want to create a ticket.", false);
                parseBranches(result, gson);
            } else if (type.equalsIgnoreCase(TYPE_CREATE_TICKET)) {
                createTicketResponse = gson.fromJson(result, CreateTicketResponse.class);
                setChatInputs("Ticket created " + "with a Reference ID: " + createTicketResponse.getId(), false);
            } else if (type.contains(TYPE_GET_ALL_TICKET)) {
                Log.d("TICKETS OF YOURS", result);
                setChatInputs("Here you go...", false);
                parseTickets(result, gson);
            }
        }

    }

    private void setChatInputs(String input, boolean align) {
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(align);
        chatPojoModel.setInput(input);
        chatPojoModel.setDepartmentResponse(null);
        chatPojoModel.setCreateTicketResponse(createTicketResponse);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
        if (!align)
            textToSpeech.speak(input, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void parseBranches(String result, Gson gson) {
        List<BranchesModel> branchesModels = Arrays.asList(gson.fromJson(result, BranchesModel[].class));

        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(false);
        chatPojoModel.setDepartmentResponse(branchesModels);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
    }

    private void parseTickets(String result, Gson gson) {
        List<GetTicketResponse> ticketResponseList = Arrays.asList(gson.fromJson(result, GetTicketResponse[].class));
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(false);
        chatPojoModel.setGetTicketResponse(ticketResponseList);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
    }

    private void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (!matches.isEmpty()) {
            setChatInputs(matches.get(0), true);
            sendRequest(matches.get(0));
        }
        recognitionProgressView.stop();
        recognitionProgressView.play();
    }

    @Override
    public void onResultSelection(String id, String branchName) {
        sendRequest(branchName);
    }

    /**
     * saving ticket
     */
    private void createTicket(Result result) {
        JSONObject ticketObject = new JSONObject();
        try {
            ticketObject.put(RequestParams.BRANCH, result.getParameters().get("department").toString().replaceAll("\"", "").replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
            ticketObject.put(RequestParams.SUBJECT, result.getParameters().get("ticketsubject").toString().replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
            ticketObject.put(RequestParams.DESCRIPTION, result.getParameters().get("ticketdesc").toString().replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", ""));
            ticketObject.put(RequestParams.STATUS, "new");
            ticketObject.put(RequestParams.PRIORITY, "high");
            ticketObject.put(RequestParams.SOURCE, "email");
            ticketObject.put(RequestParams.REPORTER, "diamante_" + sessionManager.getKeyUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServiceTask apiServiceTask = new ApiServiceTask(this, this, TYPE_CREATE_TICKET);
        apiServiceTask.setRequestParams(ticketObject, JSONParser.POST);
        apiServiceTask.execute(Constant.BASE + "tickets");
    }

    /**
     * getting all tickets
     */
    private void getTickets() {
        ApiServiceTask apiServiceTask = new ApiServiceTask(this, this, TYPE_GET_ALL_TICKET);
        apiServiceTask.setRequestParams(null, GET);
        apiServiceTask.execute(Constant.BASE + "tickets");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            AIService.getService(this, aiConfiguration).cancel();
    }

    private void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.startListening(intent);
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Constant.showToastMessage(this, getString(R.string.grant_audio));

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_RECORD_AUDIO) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
    }
}
