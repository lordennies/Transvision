package com.project.dennis.transvision.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.dennis.transvision.MySingleton;
import com.project.dennis.transvision.R;
import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.models.PermohonanResponse;
import com.project.dennis.transvision.retrofit.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class WaitingActivity extends AppCompatActivity {

    private String userIdString;
    private String peminjamanIdString;
    private Button buttonCek;
    private ProgressDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        initView();
        getPrefUser();
        builder = new AlertDialog.Builder(this);
        buttonCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekStatus();
            }
        });
    }

    private void getPrefUser() {
        SharedPreferences preferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        userIdString = preferences.getString("userId", "");
        peminjamanIdString = preferences.getString("peminjamanId", "");
        Log.d("WaitingAct", "peminjamanIdPref : "+peminjamanIdString);
    }

    private void cekStatus() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Mohon Tunggu...");
        dialog.show();

        Call<PermohonanResponse> call = Client.getInstanceRetrofit().cekStatusPermohonan(peminjamanIdString);
        call.enqueue(new Callback<PermohonanResponse>() {
            @Override
            public void onResponse(Call<PermohonanResponse> call, retrofit2.Response<PermohonanResponse> response) {
                if (response.isSuccessful()) {
                    PermohonanResponse permohonanResponse = response.body();
                    String status = permohonanResponse.getStatus();
                    dialog.dismiss();
                    switch (status) {
                        case "pending":
                            Toast.makeText(WaitingActivity.this,
                                    "Permohonan anda belum dikonfirmasi atasan",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case "diizinkan":
                            Toast.makeText(WaitingActivity.this,
                                    "Permohonan anda disetujui, silahkan menuju proses selanjutnya",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(WaitingActivity.this, UploadActivity.class));
                            finish();
                            break;
                        case "ditolak":
                            Toast.makeText(WaitingActivity.this,
                                    "Permohonan anda ditolak",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(WaitingActivity.this, MainActivity.class));
                            finish();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PermohonanResponse> call, Throwable t) {

            }
        });
    }

    private void initView() {
        buttonCek = (Button) findViewById(R.id.button_cek);
    }
}
