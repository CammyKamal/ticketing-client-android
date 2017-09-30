package com.chandigarhadmin.retrofit;

import com.chandigarhadmin.utils.Constant;
import com.chandigarhadmin.utils.User;
import com.chandigarhadmin.utils.WsseToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bimalchawla on 29/9/17.
 */

public class RetrofitApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        //logging request response header
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                //adding request headers
                User user = new User();
                user.setUsername(Constant.USERNAME);
                user.setPassword(Constant.PASSWORDH);
                WsseToken token = new WsseToken(user);
                Request request = original.newBuilder()
                        .header(WsseToken.HEADER_AUTHORIZATION, token.getAuthorizationHeader())
                        .header(WsseToken.HEADER_WSSE, token.getWsseHeader())
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        })
                .addInterceptor(interceptor).build();

        if (null == retrofit) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }

        return retrofit;
    }
}
