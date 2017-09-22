package com.example.theodhor.retrofit2;

import android.content.Context;
import android.util.Log;

import com.example.theodhor.retrofit2.Events.ErrorEvent;
import com.example.theodhor.retrofit2.Events.ServerEvent;
import com.example.theodhor.retrofit2.interfaces.ProductDataCallback;
import com.example.theodhor.retrofit2.interfaces.TopCateCallback;
import com.example.theodhor.retrofit2.interfaces.TopCategorySelectionCallback;
import com.example.theodhor.retrofit2.net.Interface;
import com.example.theodhor.retrofit2.net.ProductList;
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

public class AzureCommunicator {
    private static final String TAG = "AzureCommunicator";
    private static final String SERVER_URL = "https://commerce-search.search.windows.net";
    private static final String apiKey = "6731AD6C28D7688B13AC68FCC0DDBD4E";
    private static final String apiVersion = "2015-02-28";
    private static final String topLevelCategoryFilter = "parent eq null";
    TopCateCallback topCateCallback;
    TopCategorySelectionCallback topCategorySelectionCallback;
    ProductDataCallback productDataCallback;

    public AzureCommunicator(Context context){


        topCateCallback= (TopCateCallback) context;
        topCategorySelectionCallback= (TopCategorySelectionCallback) context;
        productDataCallback=(ProductDataCallback)context;
    }


    public void getTopCategories() {

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

        Call<TopProductModel> call = service.get(apiKey, apiVersion, topLevelCategoryFilter);

        call.enqueue(new Callback<TopProductModel>() {
            @Override
            public void onResponse(Call<TopProductModel> call, Response<TopProductModel> response) {
                // response.isSuccessful() is true if the response code is 2xx
                // BusProvider.getInstance().post(new ServerEvent(response.body()));
                topCateCallback.onSuccess(response);
                Log.e(TAG, "Success");
            }

            @Override
            public void onFailure(Call<TopProductModel> call, Throwable t) {
                // handle execution failures like no internet connectivity
                topCateCallback.onFailure(t);
                BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
            }
        });
    }

    public void getProductByCat(String subCatByCategoryName) {

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
        Call<TopProductModel> call = service.subCategoriesByCategoryName(apiKey, apiVersion,subCatByCategoryName + "~\"");

        call.enqueue(new Callback<TopProductModel>() {
            @Override
            public void onResponse(Call<TopProductModel> call, Response<TopProductModel> response) {
                // response.isSuccessful() is true if the response code is 2xx
                // BusProvider.getInstance().post(new ServerEvent(response.body()));
                topCategorySelectionCallback.onCategorySelection(response);
                Log.e(TAG, "Success");
            }

            @Override
            public void onFailure(Call<TopProductModel> call, Throwable t) {
                // handle execution failures like no internet connectivity
                topCategorySelectionCallback.onCategoryFailure(t);
                //BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
            }
        });
    }
    public void searchProduct(String productTitle) {

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
       // String productTitle = "Touring-2000";
        //to get PRODUCTS LIST for given category ID (exact ID comparison)
        Call<ProductList> call = service.findProductsByTitle(apiKey, apiVersion, productTitle  );


        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                // response.isSuccessful() is true if the response code is 2xx
               // BusProvider.getInstance().post(new ServerEvent(response.body()));
                productDataCallback.productCallback(response);
                Log.e(TAG,"Success");
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });
    }



    public void loginGet(String username, String password) {

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

        Call<TopProductModel> call = service.get("login", username, password);

        call.enqueue(new Callback<TopProductModel>() {
            @Override
            public void onResponse(Call<TopProductModel> call, Response<TopProductModel> response) {
                // response.isSuccessful() is true if the response code is 2xx
                //  BusProvider.getInstance().post(new ServerEvent(response.body()));
                Log.e(TAG, "Success");
            }

            @Override
            public void onFailure(Call<TopProductModel> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
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
