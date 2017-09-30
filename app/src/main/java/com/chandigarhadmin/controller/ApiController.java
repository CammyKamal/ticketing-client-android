package com.chandigarhadmin.controller;

import com.chandigarhadmin.App;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.interfaces.RetrofitApiInterface;
import com.chandigarhadmin.models.CreateTicketModel;
import com.chandigarhadmin.models.LoginUserModel;

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
    private Call call = null;

    //confirm otp call
    public void confirmOtp(ResponseCallback responseCallback, LoginUserModel body, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.saveLoginDetail(body);
        this.requestType = type;
        userResponse();
    }

    //get user by email call
    public void getUserByEmail(ResponseCallback responseCallback, String email, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.getUserByEmail(email + "@gmail.com");
        this.requestType = type;
        userResponse();
    }

    //get user by email call
    public void getBranches(ResponseCallback responseCallback, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.getBranches();
        this.requestType = type;
        userResponse();
    }

    //fetching all tickets by providing the user id
    public void getAllTickets(ResponseCallback responseCallback, String id, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.getAllTickets("diamante_" + id);
        this.requestType = type;
        userResponse();
    }

    //creating new ticket
    public void createTicket(ResponseCallback responseCallback, CreateTicketModel model, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.createTicket(model);
        this.requestType = type;
        userResponse();
    }

    //view ticket by providing the ticket id
    public void viewTicket(ResponseCallback responseCallback, String id, String type) {
        this.responseCallback = responseCallback;
        call = apiInterface.viewTicket(id);
        this.requestType = type;
        userResponse();
    }

    //capturing response and make appropriate action
    private void userResponse() {
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
