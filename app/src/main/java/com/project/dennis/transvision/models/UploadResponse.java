package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 8/18/2018.
 */
public class UploadResponse {

    @SerializedName("status")
    private String status;

    public UploadResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
