package com.example.theodhor.retrofit2.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Value implements Serializable {

    @SerializedName("@search.score")
    private Float searchScore;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("parent")
    private String parent;

    @SerializedName("@search.score")
    public Float getSearchScore() {
        return searchScore;
    }

    @SerializedName("@search.score")
    public void setSearchScore(Float searchScore) {
        this.searchScore = searchScore;
    }

    @SerializedName("id")
    public String getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("description")
    public String getDescription() {
        return description;
    }

    @SerializedName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("parent")
    public Object getParent() {
        return parent;
    }

    @SerializedName("parent")
    public void setParent(String parent) {
        this.parent = parent;
    }
}