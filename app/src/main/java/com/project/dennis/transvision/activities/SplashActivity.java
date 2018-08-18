package com.project.dennis.transvision.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.project.dennis.transvision.R;
import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.models.User;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private TextView emailTextView;
    private AlertDialog.Builder builder;
    private String hasMadeReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        builder = new AlertDialog.Builder(this);
        getPreferences();
        checkStatus();
    }

    private void initView() {
        emailTextView = findViewById(R.id.email_text_view);
    }

    private void getPreferences() {
        SharedPreferences preferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        hasMadeReq = preferences.getString("hasMadeReq", "");
        emailTextView.setText(email);
    }

    private void checkStatus() {
        String emailString = emailTextView.getText().toString().trim();
        Log.d("Splash", "email = "+emailString);
        // Kesini jika sudah pernah login
        if (emailString.length() > 0) {
            Log.d(TAG, "checkStatus: sudah login");
            if (isConnected(this)) {
                if (hasMadeReq.equals("") || hasMadeReq.equals("0")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Belum membuat permohonan
                            Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intentMain);
                            finish();
                        }
                    }, 1000);
                } else {
                    Intent waitingIntent = new Intent(SplashActivity.this, WaitingActivity.class);
                    startActivity(waitingIntent);
                    finish();
                }
            } else {
                displayAlert("Koneksikan device anda dengan internet");
            }
        } else { // Ke sini jika belum pernah login
            Log.d(TAG, "checkStatus: belum login");
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

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
