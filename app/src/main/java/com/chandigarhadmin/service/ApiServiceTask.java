package com.chandigarhadmin.service;

import android.content.Context;
import android.os.AsyncTask;

import com.chandigarhadmin.utils.Constant;

import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.utils.User;

import org.json.JSONObject;

/**
 * Created by kamal on 9/21/2017.
 */

public class ApiServiceTask extends AsyncTask<String, Void, String> {

    private Context context;
    private JSONObject requestParams;
    private ResponseCallback responseCallback;
    private String type;
    private int methodCallType;

    public ApiServiceTask(Context context, ResponseCallback responseCallback, String type) {
        this.context = context;
        this.type = type;
        this.responseCallback = responseCallback;
    }

    private void setRequestParams(final JSONObject jsonObject, final int methodCallType) {
        this.requestParams = jsonObject;
        this.methodCallType = methodCallType;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        JSONParser jsonParser = new JSONParser(context);
        User user = new User();
        user.setUsername(Constant.USERNAME);
        user.setPassword(Constant.PASSWORDH);
        String jsonObject = jsonParser.getResponseString(params[0], methodCallType, requestParams, null, user);

        if (null != jsonObject) {
            return jsonObject;

        }
        return "Failed";
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("RESPONSE" + result);
        responseCallback.onResponse(result, type);


    }


    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
