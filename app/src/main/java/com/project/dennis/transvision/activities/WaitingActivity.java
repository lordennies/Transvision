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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WaitingActivity extends AppCompatActivity {

    private String userId, peminjamanId;
    private Button buttonCek;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        initView();
        getPrefUser();
        mBuilder = new AlertDialog.Builder(this);
        buttonCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekStatus();
            }
        });
    }

    private void getPrefUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
        peminjamanId = sharedPreferences.getString("peminjaman_id", "");
    }

    private void cekStatus() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Mohon Tunggu...");
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, ConfigLink.STATUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String statusReq = jsonObject.getString("status");
                            if (statusReq.equals("pending")) {
                                displayAlert("Permohonan anda belum dikonfirmasi oleh admin.", statusReq);
                            } else if (statusReq.equals("diizinkan")) {
                                displayAlert("Permohonan anda disetujui. Silahkan lanjut.", statusReq);
                            } else {
                                displayAlert("Permohonan anda ditolak. Anda bisa membuat permohonan lagi.", statusReq);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.hide();
                        Toast.makeText(WaitingActivity.this, "Error bro!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(ConfigLink.USER_ID, userId);
                params.put(ConfigLink.PEMINJAMAN_ID, peminjamanId);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initView() {
        buttonCek = (Button) findViewById(R.id.button_cek);
    }

    public void displayAlert(String message, final String statusReq) {
        mBuilder.setMessage(message);
        mBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    if (statusReq.equals("pending")) {
                        dialog.dismiss();
                    } else if (statusReq.equals("diizinkan")) {
                        dialog.dismiss();
                        Intent intentUpload = new Intent(WaitingActivity.this, UploadActivity.class);
                        startActivity(intentUpload);
                        finishAffinity();
                    } else if (statusReq.equals("ditolak")) {
                        dialog.dismiss();
                        Intent intentMain = new Intent(WaitingActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        finishAffinity();
                    }
                }
            }
        });
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }
}
