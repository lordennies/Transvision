package com.project.dennis.transvision.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailEditText, mPasswordEditText;

    private Button loginButton;

    private String url = ConfigLink.login;

    private AlertDialog.Builder builder;

    private String mUserId, mUsername, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        builder = new AlertDialog.Builder(this);
        loginButton.setOnClickListener(this);

        getPrefUser();
        prefNotNull();

        mEmailEditText.setText("dennis@gmail.com");
        mPasswordEditText.setText("dennis");
    }

    private void initView() {
        mEmailEditText = findViewById(R.id.edit_user_email);
        mPasswordEditText = findViewById(R.id.edit_user_password);
        loginButton = findViewById(R.id.button_login);
    }

    private void getPrefUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.loginPref, MODE_PRIVATE);
        mUserId = sharedPreferences.getString("user_id", "");
        mUsername = sharedPreferences.getString("username", "");
        mEmail = sharedPreferences.getString("email", "");

        mEmailEditText.setText(mEmail);
    }

    private void prefNotNull() {
        String emailString = mEmailEditText.getText().toString().trim();
        if (emailString.length() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            loginCheck();
        }
    }

    private void loginCheck() {
        final String emailString = mEmailEditText.getText().toString().trim();
        final String passwordString = mPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(emailString) && TextUtils.isEmpty(passwordString)) {
            displayAlert("Masukkan email dan password dengan benar");
            return;
        }

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if (status.equals("failed")) {
                                displayAlert(jsonObject.getString("message"));
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                String userIdString = jsonObject.getString("user_id");
                                String usernameString = jsonObject.getString("user_id");
                                String emailString = jsonObject.getString("user_id");
                                saveAttribute(userIdString, usernameString, emailString);
                                finish();
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error bro!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailString);
                params.put("password", passwordString);
                return params;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    private void saveAttribute(String user_id, String username, String email) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConfigLink.loginPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.commit();
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                mEmailEditText.setText("");
                mPasswordEditText.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
