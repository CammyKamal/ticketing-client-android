package com.chandigarhadmin.interfaces;

import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.LoginUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bimalchawla on 29/9/17.
 */

public interface RetrofitApiInterface {

    @POST("users")
    Call<CreateUserResponse> saveLoginDetail(@Body LoginUser body);

    @GET("users")
    Call<List<CreateUserResponse>> getAllUsers();

    @GET("users/{email}/")
    Call<CreateUserResponse> getUserByEmail(@Path("email") String email);

}
