package com.chandigarhadmin.interfaces;


import retrofit2.Response;

public interface ResponseCallback {
    void onResponse(Response response, String type);

    void onFailure(String message);
}
