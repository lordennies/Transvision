package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
