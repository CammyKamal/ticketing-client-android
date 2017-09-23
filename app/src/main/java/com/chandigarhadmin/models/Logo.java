package com.chandigarhadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Logo implements Serializable{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pathname")
    @Expose
    private String pathname;
    @SerializedName("original_name")
    @Expose
    private String originalName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
