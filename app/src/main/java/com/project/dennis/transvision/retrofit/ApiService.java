package com.project.dennis.transvision.retrofit;

import com.project.dennis.transvision.models.InsertResponse;
import com.project.dennis.transvision.models.LoginResponse;
import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.models.PermohonanResponse;
import com.project.dennis.transvision.models.UploadResponse;
import com.project.dennis.transvision.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("pinjam")
    Call<ArrayList<Peminjaman>> getPeminjaman(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("pinjam")
    Call<InsertResponse> addNewPeminjaman(
            @Field("action") String action,
            @Field("user_id") String userId,
            @Field("tujuan") String tujuan,
            @Field("keperluan") String keperluan,
            @Field("jum_penumpang") String jumPenumpang,
            @Field("tgl_pemakaian") String tglPemakaian
    );

    @FormUrlEncoded
    @POST("cekStatusPermohonan")
    Call<PermohonanResponse> cekStatusPermohonan(@Field("peminjaman_id") String peminjamanId);

    @FormUrlEncoded
    @POST("upload")
    Call<UploadResponse> upload(
            @Field("image") String image,
            @Field("peminjaman_id") String peminjamanId
    );
}
