package com.project.dennis.transvision.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.MySingleton;
import com.project.dennis.transvision.R;
import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.retrofit.ApiService;
import com.project.dennis.transvision.retrofit.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ConfirmationActivity extends AppCompatActivity {

    private Button kirimButton;
    private TextView tujuanTextView;
    private TextView keperluanTextView;
    private TextView jumPenumpangTextView;
    private TextView tglPemakaianTextView;
    private String userIdString;
    private String tujuanString;
    private String keperluanString;
    private String jumPenumpangString;
    private String tglPemakaianString;
    private String peminjamanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        initView();
        getData();
        displayData();
        kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimRequest();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        userIdString = sharedPreferences.getString("userId", "");
    }

    private void initView() {
        tujuanTextView = findViewById(R.id.tv_tujuan);
        keperluanTextView = findViewById(R.id.tv_keperluan);
        jumPenumpangTextView = findViewById(R.id.tv_jum_penumpang);
        tglPemakaianTextView = findViewById(R.id.tv_tgl_pemakaian);
        kirimButton = findViewById(R.id.btn_kirim);
    }

    /* Mengambil data menggunakan intent dari EditorActivity */
    private void getData() {
        Intent editorIntent = getIntent();
        Peminjaman peminjaman = editorIntent.getParcelableExtra("Permohonan");

        tujuanString = peminjaman.getTujuan();
        keperluanString = peminjaman.getKeperluan();
        jumPenumpangString = peminjaman.getJumPenumpang();
        tglPemakaianString = peminjaman.getTglPemakaian();
    }

    /* Menampilkan data sebelum dikirim ke database */
    private void displayData() {
        tujuanTextView.setText(tujuanString);
        keperluanTextView.setText(keperluanString);
        jumPenumpangTextView.setText(jumPenumpangString);
        tglPemakaianTextView.setText(tglPemakaianString);
    }

    private void kirimRequest() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Mohon Tunggu...");
        mProgressDialog.show();

        ApiService apiService = Client.getInstanceRetrofit();
        Call<ResponseBody> call = apiService.addNewPeminjaman(
                "create",
                userIdString,
                tujuanString,
                keperluanString,
                jumPenumpangString,
                tglPemakaianString
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mProgressDialog.dismiss();
                    Intent intentWaitingAct = new Intent(ConfirmationActivity.this, WaitingActivity.class);
                    startActivity(intentWaitingAct);
                    finishAffinity();
                    String message = "Permohonan sudah dikirim";
                    Toast.makeText(ConfirmationActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ConfirmationActivity.this, "Permohonan gagal dikirim", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAttribute() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConfigLink.PEMINJAMAN_ID, peminjamanId);
        editor.apply();
    }
}
