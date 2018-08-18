package com.project.dennis.transvision.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("has_made_req")
    private String hasMadeReq;
    @SerializedName("peminjaman_id")
    private String peminjamanId;

    public User(String userId, String username, String email, String hasMadeReq, String peminjamanId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.hasMadeReq = hasMadeReq;
        this.peminjamanId = peminjamanId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHasMadeReq() {
        return hasMadeReq;
    }

    public String getPeminjamanId() {
        return peminjamanId;
    }
}
