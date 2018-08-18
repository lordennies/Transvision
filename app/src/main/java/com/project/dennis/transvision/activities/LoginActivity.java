package com.project.dennis.transvision.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.R;
import com.project.dennis.transvision.models.LoginResponse;
import com.project.dennis.transvision.models.User;
import com.project.dennis.transvision.retrofit.ApiService;
import com.project.dennis.transvision.retrofit.Client;
 
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AlertDialog.Builder builder;
    private ProgressDialog dialog;
    private String userIdString;
    private String emailString;
    private String peminjamanIdString;
    private String hasMadeReqString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        builder = new AlertDialog.Builder(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                loginCheck();
            }
        });
    }

    private void initView() {
        loginButton = findViewById(R.id.button_login);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loginCheck() {
        // Mengambil value dari edit text di login page
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        // Cek apakah user mengosongkan salah satu atau kedua edit text
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            displayAlert("Masukkan email dan password dengan benar");
            return;
        }
        // Menampilkan dialog loading
        dialog = new ProgressDialog(this);
        dialog.setMessage("Mohon Tunggu...");
        dialog.show();
        // Melakukan pengecekan login
        Call<LoginResponse> call = Client.getInstanceRetrofit().login(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getStatus().equals("success")) {
                        dialog.dismiss();
                        userIdString = loginResponse.getUser().getUserId();
                        emailString = loginResponse.getUser().getEmail();
                        hasMadeReqString = loginResponse.getUser().getHasMadeReq();
                        peminjamanIdString = loginResponse.getUser().getPeminjamanId();
                        Log.d(TAG, "userId = "+userIdString+"\nemail = "+emailString+"\npeminjamanId = "+peminjamanIdString);
                        // Menyimpan data user ke SharedPreferences
                        saveAttribute();
                        if (hasMadeReqString.equals("0")) {
                            // Menuju MainActivity jika belum membuat permohonan
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else if (hasMadeReqString.equals("1")) {
                            // Menuju WaitingActivity jika sudah membuat permohonan
                            Intent waitingIntent = new Intent(LoginActivity.this, WaitingActivity.class);
                            startActivity(waitingIntent);
                            finish();
                        }
                    } else if (loginResponse.getStatus().equals("failed")) {
                        dialog.dismiss();
                        displayAlert("Email atau password anda salah");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.dismiss();
                displayAlert("Device anda tidak terkoneksi internet");
            }
        });
    }

    private void saveAttribute() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userIdString);
        editor.putString("email", emailString);
        editor.putString("hasMadeReq", hasMadeReqString);
        editor.putString("peminjamanId", peminjamanIdString);
        editor.apply();
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                emailEditText.setText("");
                passwordEditText.setText("");
                emailEditText.requestFocus();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
