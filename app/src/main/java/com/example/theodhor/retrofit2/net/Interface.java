package com.example.theodhor.retrofit2.net;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dori on 12/28/2016.
 */

public interface Interface {

    @FormUrlEncoded
    @POST("/sp/index.php")
    Call<ServerResponse> post(
            @Field("method") String method,
            @Field("USERNAME") String username,
            @Field("password") String password
    );

    //Azure search API
    @GET("/indexes/categories/docs")
    Call<TopProductModel> get(
            @Header("api-key") String apiKey,
            @Query("api-version") String apiVersion,
            @Query("$filter") String filter
    );

    @GET("/indexes/categories/docs")
    Call<TopProductModel> subCategoriesByCategoryName(
            @Header("api-key") String apiKey,
            @Query("api-version") String apiVersion,
            @Query("search") String filter
    );

    @GET("/indexes/categories/docs")
    Call<ServerResponse> subcategoriesByParentId(
            @Header("api-key") String apiKey,
            @Query("api-version") String apiVersion,
            @Query("$filter") String filter
    );

    @GET("/indexes/products/docs")
    Call<ProductList> findProductsByTitle(
            @Header("api-key") String apiKey,
            @Query("api-version") String apiVersion,
            @Query("search") String filter
    );

}