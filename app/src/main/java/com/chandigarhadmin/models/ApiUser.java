package com.chandigarhadmin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class ApiUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("roles")
    @Expose
    private List<Object> roles = null;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("hash_expiration_time")
    @Expose
    private String hashExpirationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Object> getRoles() {
        return roles;
    }

    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashExpirationTime() {
        return hashExpirationTime;
    }

    public void setHashExpirationTime(String hashExpirationTime) {
        this.hashExpirationTime = hashExpirationTime;
    }
}
