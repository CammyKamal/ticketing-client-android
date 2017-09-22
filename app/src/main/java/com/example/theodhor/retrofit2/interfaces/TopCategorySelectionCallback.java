package com.example.theodhor.retrofit2.interfaces;


import com.example.theodhor.retrofit2.net.TopProductModel;

import retrofit2.Response;

public interface TopCategorySelectionCallback {
    void onCategorySelection(Response<TopProductModel> response);

    void onCategoryFailure(Throwable t);
}
