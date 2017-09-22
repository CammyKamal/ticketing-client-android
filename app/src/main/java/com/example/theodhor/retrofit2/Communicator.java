package com.example.theodhor.retrofit2;

import android.util.Log;

import com.example.theodhor.retrofit2.Events.ErrorEvent;
import com.example.theodhor.retrofit2.Events.ServerEvent;
import com.example.theodhor.retrofit2.net.Interface;
import com.example.theodhor.retrofit2.net.ServerResponse;
import com.example.theodhor.retrofit2.net.TopProductModel;
import com.squareup.otto.Produce;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dori on 12/28/2016.
 */

public class Communicator {
    private static  final String TAG = "Communicator";
    private static final String SERVER_URL = "http://www.sample.al";

    public void loginPost(String username, String password){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        Interface service = retrofit.create(Interface.class);

        Call<ServerResponse> call = service.post("login",username,password);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                // response.isSuccessful() is true if the response code is 2xx
                BusProvider.getInstance().post(new ServerEvent(response.body()));
                Log.e(TAG,"Success");
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });
    }

    public void loginGet(String username, String password){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        Interface service = retrofit.create(Interface.class);

        Call<TopProductModel> call = service.get("login",username,password);

        call.enqueue(new Callback<TopProductModel>() {
            @Override
            public void onResponse(Call<TopProductModel> call, Response<TopProductModel> response) {
                // response.isSuccessful() is true if the response code is 2xx
              //  BusProvider.getInstance().post(new ServerEvent(response.body()));
                Log.e(TAG,"Success");
            }

            @Override
            public void onFailure(Call<TopProductModel> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });
    }



    @Produce
    public ServerEvent produceServerEvent(ServerResponse serverResponse) {
        return new ServerEvent(serverResponse);
    }

    @Produce
    public ErrorEvent produceErrorEvent(int errorCode, String errorMsg) {
        return new ErrorEvent(errorCode, errorMsg);
    }
}
