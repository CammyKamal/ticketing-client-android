package com.example.theodhor.retrofit2.interfaces;


import com.example.theodhor.retrofit2.net.ProductList;

import retrofit2.Response;

public interface ProductDataCallback {
    void productCallback( Response<ProductList> response);
    void productFailure(Throwable throwable);
}
