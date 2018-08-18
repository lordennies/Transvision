package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 8/18/2018.
 */
public class PermohonanResponse {

    @SerializedName("status")
    private String status;

    public PermohonanResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
