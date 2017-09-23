package com.chandigarhadmin.ui;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import com.chandigarhadmin.service.ApiServiceTask;
import com.chandigarhadmin.models.BranchesModel;
import com.chandigarhadmin.utils.Constant;
import com.chandigarhadmin.interfaces.ResponseCallback;

public class AdminAgentActivity extends Activity implements AIListener,ResponseCallback {
    private AIService aiService;
    final AIConfiguration config = new AIConfiguration("0882e9aa21ef450694115050513f3822",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        sendRequest("ticket");
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
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        if(result.getAction().equalsIgnoreCase("createticket")){

            if(response.getResult().getFulfillment().getSpeech().equalsIgnoreCase("callDepartmentApi")){
                new ApiServiceTask(AdminAgentActivity.this,this,"branches").execute(Constant.BASE + "branches");
            }
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
        Gson  gson = gsonBuilder.create();

        if(type.equalsIgnoreCase("branches")){
            parseBranches(result,gson);
        }

    }

    private void parseBranches(String result, Gson  gson ) {
        List<BranchesModel> posts = Arrays.asList(gson.fromJson(result, BranchesModel[].class));


    }
}
