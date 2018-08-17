package com.project.dennis.transvision.retrofit;

import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.models.Result;
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

    @POST("login")
    Call<User> login(@Query("email") String email, @Query("password") String password);

    @POST("hasMadeRequest")
    Call<Result> hasMadeRequest(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("pinjam")
    Call<ResponseBody> addNewPeminjaman(
            @Field("action") String action,
            @Field("user_id") String userId,
            @Field("tujuan") String tujuan,
            @Field("keperluan") String keperluan,
            @Field("jum_penumpang") String jumPenumpang,
            @Field("tgl_pemakaian") String tglPemakaian
    );
}
