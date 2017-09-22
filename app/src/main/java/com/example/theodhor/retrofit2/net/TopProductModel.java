package com.example.theodhor.retrofit2.net;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopProductModel {

  @SerializedName("@odata.context")
    private String odataContext;
    @SerializedName("value")
    private List<Value> value = null;
    private final static long serialVersionUID = 2308573609996227046L;

    @SerializedName("@odata.context")
    public String getOdataContext() {
        return odataContext;
    }

    @SerializedName("@odata.context")
    public void setOdataContext(String odataContext) {
        this.odataContext = odataContext;
    }

    @SerializedName("value")
    public List<Value> getValue() {
        return value;
    }

    @SerializedName("value")
    public void setValue(List<Value> value) {
        this.value = value;
    }
}
