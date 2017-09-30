package com.chandigarhadmin.controller;

import com.chandigarhadmin.App;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.interfaces.RetrofitApiInterface;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.LoginUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bimalchawla on 29/9/17.
 */

public class ApiController {

    private RetrofitApiInterface apiInterface = App.getInterface();
    private ResponseCallback responseCallback;
    private String requestType = null;

    //confirm otp call
    public void confirmOtp(ResponseCallback responseCallback, LoginUser body, String type) {
        this.responseCallback = responseCallback;
        Call<CreateUserResponse> call = apiInterface.saveLoginDetail(body);
        this.requestType = type;
        userResponse(call);
    }

    //get user by email call
    public void getUserByEmail(ResponseCallback responseCallback, String email, String type) {
        this.responseCallback = responseCallback;
        Call call = apiInterface.getUserByEmail(email + "@gmail.com");
        this.requestType = type;
        userResponse(call);
    }

    //get all users
    public void getAllUsers(ResponseCallback responseCallback, String type) {
        this.responseCallback = responseCallback;
        Call call = apiInterface.getAllUsers();
        this.requestType = type;
        getUserResponse(call);
    }

    //capturing response and make appropriate action
    private void userResponse(Call<CreateUserResponse> call) {
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                responseCallback.onResponse(response, requestType);
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                call.cancel();
                responseCallback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    //capturing response and make appropriate action
    private void getUserResponse(Call call) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                responseCallback.onResponse(response, requestType);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                responseCallback.onFailure(t.getLocalizedMessage());
            }
        });
    }
}
