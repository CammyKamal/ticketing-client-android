package com.chandigarhadmin.ui;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static com.chandigarhadmin.models.RequestParams.TYPE_CREATE_TICKET;
import static com.chandigarhadmin.models.RequestParams.TYPE_GET_ALL_TICKET;
import static com.chandigarhadmin.models.RequestParams.TYPE_GET_BRANCHES;
import static com.chandigarhadmin.service.JSONParser.GET;

public class AdminAgentActivity extends Activity implements AIListener, ResponseCallback, View.OnClickListener, SelectionCallbacks {
    private AIService aiService;

    private AIConfiguration aiConfiguration;
    private ArrayList<ChatPojoModel> chatBotResponseList;

    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private EditText etInputBox;
    private Button btnSearch;
    private String ticketSubject, ticketDesc, ticketid;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        sessionManager = new SessionManager(this);
        initializeAI();
        initializeViews();
        setChatInputs("Hi, How are you?<br/>How may i help you?", false);
    }


    //Method to initialize AI
    private void initializeAI() {
        aiConfiguration = new AIConfiguration(Constant.AI_CONFIGURATION_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        if (aiService != null) {
            aiService.resetContexts();
        }
        aiService = AIService.getService(this, aiConfiguration);
        aiService.setListener(this);
    }

    //Method to initialize recyclerview
    private void initializeViews() {
        chatBotResponseList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        etInputBox = (EditText) findViewById(R.id.querystringet);
        btnSearch = (Button) findViewById(R.id.searchbtn);
        btnSearch.setOnClickListener(this);
        mAdapter = new ChatAdapter(this, chatBotResponseList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    //AIRequest should have query OR event
    private void sendRequest(String queryString) {
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
                CreateTicketResponse createTicketResponse = gson.fromJson(result, CreateTicketResponse.class);
                setChatInputs("Ticket created \n" + " Refrence ID: " + createTicketResponse.getId(), false);
            } else if (type.contains(TYPE_GET_ALL_TICKET)) {
                Log.d("TICKETS OF YOURS", result);
                parseTickets(result, gson);
            }
        }

    }

    private void setChatInputs(String input, boolean align) {
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(align);
        chatPojoModel.setInput(input);
        chatPojoModel.setDepartmentResponse(null);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
    }

    private void parseBranches(String result, Gson gson) {
        List<BranchesModel> branchesModels = Arrays.asList(gson.fromJson(result, BranchesModel[].class));
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(false);
        chatPojoModel.setDepartmentResponse(branchesModels);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatBotResponseList.size() - 1);
    }

    private void parseTickets(String result, Gson gson) {
        List<GetTicketResponse> ticketResponseList = Arrays.asList(gson.fromJson(result, GetTicketResponse[].class));
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setAlignRight(false);
        chatPojoModel.setGetTicketResponse(ticketResponseList);
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchbtn:
                if (!etInputBox.getText().toString().equalsIgnoreCase("")) {
                    String input = etInputBox.getText().toString();
                    setChatInputs(input, true);
                    etInputBox.setText("");
                    sendRequest(input);
                }
                break;
            default:
                break;
        }
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
            ticketObject.put(RequestParams.REPORTER, "diamante_"+sessionManager.getKeyUserId());
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
        if (AIService.getService(this, aiConfiguration) != null) {
            AIService.getService(this, aiConfiguration).cancel();
        }
    }
}
