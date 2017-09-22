package com.example.theodhor.retrofit2.ai;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theodhor.retrofit2.AzureCommunicator;
import com.example.theodhor.retrofit2.R;
import com.example.theodhor.retrofit2.adapter.ChatAdapter;
import com.example.theodhor.retrofit2.interfaces.ProductDataCallback;
import com.example.theodhor.retrofit2.interfaces.TopCateCallback;
import com.example.theodhor.retrofit2.interfaces.TopCategorySelectionCallback;
import com.example.theodhor.retrofit2.net.ChatPojoModel;
import com.example.theodhor.retrofit2.net.ProductList;
import com.example.theodhor.retrofit2.net.TopProductModel;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import retrofit2.Response;

public class AgentActivity extends AppCompatActivity implements AIListener, TopCateCallback,
        TopCategorySelectionCallback, ProductDataCallback {
    private Button listenButton, searchbtn;
    private TextView resultTextView;
    ArrayList<ChatPojoModel> chatBotResponseList;
    TextToSpeech t1;
    private AIService aiService;
    private EditText querystringet;
    LayoutInflater layoutInflater;
    final AIConfiguration config = new AIConfiguration("841360bf191d408d81396c775a7efb83",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    private AzureCommunicator communicator;
    LinearLayout chatView;
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        chatBotResponseList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ChatAdapter(this,chatBotResponseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        chatView = (LinearLayout) findViewById(R.id.chatviewll);
        communicator = new AzureCommunicator(this);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        listenButton = (Button) findViewById(R.id.listenButton);
        querystringet = (EditText) findViewById(R.id.querystringet);
        searchbtn = (Button) findViewById(R.id.searchbtn);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = layoutInflater.inflate(R.layout.row_chat_layout, null);
                // fill in any details dynamically here
                TextView textView = (TextView) view.findViewById(R.id.resultTextView);
                textView.setVisibility(View.GONE);
                ((LinearLayout) view.findViewById(R.id.responsecontainerll)).setVisibility(View
                        .GONE);
                TextView myquerytext = (TextView) view.findViewById(R.id.myquerytext);
                myquerytext.setText(querystringet.getText().toString());
                ((LinearLayout) findViewById(R.id.chatviewll)).addView(view);
                sendRequest(querystringet.getText().toString());
                ChatPojoModel chatPojoModel = new ChatPojoModel();
                chatPojoModel.setResponse(null);
                chatPojoModel.setType("userinput");
                chatPojoModel.setUserInput(querystringet.getText().toString());
                chatBotResponseList.add(chatPojoModel);
                mAdapter.notifyDataSetChanged();
                querystringet.setText("");


            }
        });

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }
        if (result.getAction() != null && !result.getAction().equalsIgnoreCase("")) {
            if (result.getAction().equalsIgnoreCase("showTopCategories")) {
                communicator.getTopCategories();
            } else if (result.getAction().equalsIgnoreCase("search")) {
                if (result.isActionIncomplete()) {
                    communicator.searchProduct(result.getResolvedQuery());
                } else {
                    communicator.getProductByCat(result.getResolvedQuery());
                }
            }
        }

    }

    @Override
    public void onError(ai.api.model.AIError error) {
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


    /*
* AIRequest should have query OR event
*/
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
    public void onSuccess(Response<TopProductModel> topProductModelResponse) {
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setResponse(topProductModelResponse);
        chatPojoModel.setType("showCategories");
        chatPojoModel.setUserInput("");
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
        /*View view = layoutInflater.inflate(R.layout.row_chat_layout, null);

        // fill in any details dynamically here
        TextView textView = (TextView) view.findViewById(R.id.resultTextView);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Here what we got for you...");
        LinearLayout topCatContainer = (LinearLayout) view.findViewById(R.id.responsecontainerll);
        for (int i = 0; i < topProductModelResponse.body().getValue().size(); i++) {
            View topCatView = layoutInflater.inflate(R.layout.row_top_cat, null);
            final Button button = (Button) topCatView.findViewById(R.id.topcatbtn);
            button.setText(topProductModelResponse.body().getValue().get(i).getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AgentActivity.this, button.getText().toString(), Toast
                            .LENGTH_LONG).show();
                    communicator.getProductByCat(button.getText().toString());
                }
            });
            //btnTag.setId(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 10, 20, 20);
            button.setLayoutParams(layoutParams);
            button.setTextColor(Color.BLACK);
            topCatContainer.addView(topCatView);
        }
        TextView myquerytext = (TextView) view.findViewById(R.id.myquerytext);
        myquerytext.setVisibility(View.GONE);
        chatView.addView(view);
        ((ScrollView) findViewById(R.id.scrollview)).post(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.scrollview)).fullScroll(View.FOCUS_DOWN);
            }
        });*/
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onCategorySelection(Response<TopProductModel> topProductModelResponse) {
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setResponse(topProductModelResponse);
        chatPojoModel.setType("productsTitles");
        chatPojoModel.setUserInput("");
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();

/*
        View view = layoutInflater.inflate(R.layout.row_herocard, null);
        TextView headertext = (TextView) view.findViewById(R.id.headertext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 25, 0, 0);
        view.setLayoutParams(params);
        headertext.setText("Please select a category you looking for ...");
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.carviewll);
        for (int i = 0; i < topProductModelResponse.body().getValue().size(); i++) {
            View topCatView = layoutInflater.inflate(R.layout.row_top_cat, null);
            final Button button = (Button) topCatView.findViewById(R.id.topcatbtn);
            button.setText(topProductModelResponse.body().getValue().get(i).getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AgentActivity.this, button.getText().toString(), Toast
                            .LENGTH_LONG).show();
                    communicator.searchProduct(button.getText().toString());
                }
            });
            if (topProductModelResponse.body().getValue().get(i).getParent() != null &&
                    !topProductModelResponse.body().getValue().get(i).getParent().equals("null")) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 10, 20, 20);
                button.setLayoutParams(layoutParams);
                button.setTextColor(Color.BLACK);
                linearLayout.addView(topCatView);
            }

        }

        chatView.addView(view);

        ((ScrollView) findViewById(R.id.scrollview)).post(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.scrollview)).fullScroll(View.FOCUS_DOWN);
            }
        });*/
    }

    @Override
    public void onCategoryFailure(Throwable throwable) {

    }

    @Override
    public void productCallback(Response<ProductList> response) {
        ChatPojoModel chatPojoModel = new ChatPojoModel();
        chatPojoModel.setResponse(null);
        chatPojoModel.setProductSearchResponse(response);
        chatPojoModel.setType("productSearch");
        chatPojoModel.setUserInput("");
        chatBotResponseList.add(chatPojoModel);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(AgentActivity.this, response.body().getValue().size() + "Products Found.",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void productFailure(Throwable throwable) {

    }
}
