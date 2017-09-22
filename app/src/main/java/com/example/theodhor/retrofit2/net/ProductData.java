package com.example.theodhor.retrofit2.net;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductData implements Serializable
{

    @SerializedName("@search.score")
    private Float searchScore;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("category")
    private String category;
    @SerializedName("categoryId")
    private String categoryId;
    @SerializedName("subcategory")
    private String subcategory;
    @SerializedName("subcategoryId")
    private String subcategoryId;
    @SerializedName("modifiers")
    private List<String> modifiers = null;
    @SerializedName("color")
    private List<String> color = null;
    @SerializedName("size")
    private List<String> size = null;
    @SerializedName("price")
    private Float price;
    @SerializedName("image_domain")
    private String imageDomain;
    @SerializedName("image_suffix")
    private String imageSuffix;
    private final static long serialVersionUID = 5842749390123503931L;

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

    @SerializedName("category")
    public String getCategory() {
        return category;
    }

    @SerializedName("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("categoryId")
    public String getCategoryId() {
        return categoryId;
    }

    @SerializedName("categoryId")
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @SerializedName("subcategory")
    public String getSubcategory() {
        return subcategory;
    }

    @SerializedName("subcategory")
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @SerializedName("subcategoryId")
    public String getSubcategoryId() {
        return subcategoryId;
    }

    @SerializedName("subcategoryId")
    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    @SerializedName("modifiers")
    public List<String> getModifiers() {
        return modifiers;
    }

    @SerializedName("modifiers")
    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    @SerializedName("color")
    public List<String> getColor() {
        return color;
    }

    @SerializedName("color")
    public void setColor(List<String> color) {
        this.color = color;
    }

    @SerializedName("size")
    public List<String> getSize() {
        return size;
    }

    @SerializedName("size")
    public void setSize(List<String> size) {
        this.size = size;
    }

    @SerializedName("price")
    public Float getPrice() {
        return price;
    }

    @SerializedName("price")
    public void setPrice(Float price) {
        this.price = price;
    }

    @SerializedName("image_domain")
    public String getImageDomain() {
        return imageDomain;
    }

    @SerializedName("image_domain")
    public void setImageDomain(String imageDomain) {
        this.imageDomain = imageDomain;
    }

    @SerializedName("image_suffix")
    public String getImageSuffix() {
        return imageSuffix;
    }

    @SerializedName("image_suffix")
    public void setImageSuffix(String imageSuffix) {
        this.imageSuffix = imageSuffix;
    }

}
