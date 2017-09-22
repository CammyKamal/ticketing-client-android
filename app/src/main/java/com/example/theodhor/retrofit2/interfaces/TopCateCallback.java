package com.example.theodhor.retrofit2.interfaces;


import com.example.theodhor.retrofit2.net.TopProductModel;

import retrofit2.Response;

public interface TopCateCallback {
    void onSuccess(Response<TopProductModel> topProductModelResponse);
    void onFailure(Throwable throwable);
}
