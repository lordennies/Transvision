package com.project.dennis.transvision.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.project.dennis.transvision.models.Result;
import com.project.dennis.transvision.retrofit.ApiService;
import com.project.dennis.transvision.retrofit.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SplashActivity extends AppCompatActivity {

    private String userIdString;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        getPreferences();
        checkStatus();
    }

    private void initView() {
        emailTextView = findViewById(R.id.email_text_view);
    }

    private void getPreferences() {
        SharedPreferences preferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        userIdString = preferences.getString("userId", "");
        emailTextView.setText(email);
    }

    private void checkStatus() {
        String emailString = emailTextView.getText().toString().trim();
        // Kesini jika sudah pernah login
        if (emailString.length() > 0) {
            if (!isConnected(this)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                }, 1000);
            } else {
                hasMadeRequest();
            }
        } else { // Ke sini jika belum pernah login
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }, 1000);
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) ||
                    (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void hasMadeRequest() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.hasMadeRequest(userIdString).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("able")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }, 1000);
                    } else if (response.body().getStatus().equals("unable")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent waitingIntent = new Intent(SplashActivity.this, WaitingActivity.class);
                                startActivity(waitingIntent);
                                finish();
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Gimana nih", Toast.LENGTH_SHORT).show();
                Log.d("Splash", t.getLocalizedMessage());
            }
        });
    }
}
