package com.project.dennis.transvision.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.dennis.transvision.Data.ConfigLink;
import com.project.dennis.transvision.MySingleton;
import com.project.dennis.transvision.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mKirimButton;
    private TextView mTujuanText, mKeperluanText, mJumPenumpangText, mTglPemakaianText;
    private String userIdString, tujuanString, keperluanString, jumPenumpangString, tglPemakaianString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        initView();
        getData();
        displayData();
        mKirimButton.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        userIdString = sharedPreferences.getString("user_id", "");
    }

    private void initView() {
        mTujuanText = findViewById(R.id.tv_tujuan);
        mKeperluanText = findViewById(R.id.tv_keperluan);
        mJumPenumpangText = findViewById(R.id.tv_jum_penumpang);
        mTglPemakaianText = findViewById(R.id.tv_tgl_pemakaian);
        mKirimButton = findViewById(R.id.btn_kirim);
    }

    /* Mengambil data menggunakan intent dari EditorActivity */
    private void getData() {
        Intent intentEditorAct = getIntent();
        tujuanString = intentEditorAct.getStringExtra("tujuan");
        keperluanString = intentEditorAct.getStringExtra("keperluan");
        jumPenumpangString = intentEditorAct.getStringExtra("jum_penumpang");
        tglPemakaianString = intentEditorAct.getStringExtra("tgl_pemakaian");
    }

    /* Menampilkan data sebelum dikirim ke database */
    private void displayData() {
        mTujuanText.setText(tujuanString);
        mKeperluanText.setText(keperluanString);
        mJumPenumpangText.setText(jumPenumpangString);
        mTglPemakaianText.setText(tglPemakaianString);
    }

    @Override
    public void onClick(View view) {
        if (view == mKirimButton) {
            kirimRequest();
        }
    }

    private void kirimRequest() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Mohon Tunggu...");
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, ConfigLink.PEMINJAMAN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                saveAttribute();
                                Intent intentWaitingAct = new Intent(ConfirmationActivity.this, WaitingActivity.class);
                                startActivity(intentWaitingAct);
                                finishAffinity();
                                Toast.makeText(ConfirmationActivity.this, "Permohonan ditambahkan",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ConfirmationActivity.this, "Permohonan gagal ditambahkan",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.hide();
                        Toast.makeText(ConfirmationActivity.this, "Error bro!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(ConfigLink.ACTION, "create");
                params.put(ConfigLink.USER_ID, userIdString);
                params.put(ConfigLink.TUJUAN, tujuanString);
                params.put(ConfigLink.KEPERLUAN, keperluanString);
                params.put(ConfigLink.JUM_PENUMPANG, jumPenumpangString);
                params.put(ConfigLink.TGL_PEMAKAIAN, tglPemakaianString);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void saveAttribute() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("has_made_req", "1");
        editor.commit();
    }
}
