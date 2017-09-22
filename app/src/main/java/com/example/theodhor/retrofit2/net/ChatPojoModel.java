package com.example.theodhor.retrofit2.net;


import retrofit2.Response;

public class ChatPojoModel {
    Response<TopProductModel> response;

    public Response<ProductList> getProductSearchResponse() {
        return productSearchResponse;
    }

    public void setProductSearchResponse(Response<ProductList> productSearchResponse) {
        this.productSearchResponse = productSearchResponse;
    }

    Response<ProductList> productSearchResponse;
    String type;

    public Response<TopProductModel> getResponse() {
        return response;
    }

    public void setResponse(Response<TopProductModel> response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserInput() {
        return UserInput;
    }

    public void setUserInput(String userInput) {
        UserInput = userInput;
    }

    String UserInput;
}
