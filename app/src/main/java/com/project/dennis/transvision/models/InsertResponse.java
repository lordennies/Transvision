package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dennis on 8/18/2018.
 */
public class InsertResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("peminjaman_id")
    private String peminjamanId;

    public InsertResponse(String status, String peminjamanId) {
        this.status = status;
        this.peminjamanId = peminjamanId;
    }

    public String getStatus() {
        return status;
    }

    public String getPeminjamanId() {
        return peminjamanId;
    }
}
