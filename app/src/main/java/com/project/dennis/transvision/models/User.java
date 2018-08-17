package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("status")
    private String status;

    @SerializedName("email")
    private String email;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }
}
