package com.example.theodhor.retrofit2.net;

import java.io.Serializable;

/**
 * Created by Dori on 12/28/2016.
 */
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponse implements Serializable{
    @SerializedName("returned_username")
    private String username;
    @SerializedName("returned_password")
    private String password;
    @SerializedName("message")
    private String message;
    @SerializedName("error_code")
    private int errorCode;
    private int status = 1;
    private String error;

    public ServerResponse(String username, String password, String message, int errorCode, int status, String error){
        this.username = username;
        this.password = password;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.error = error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }





}