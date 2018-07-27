package com.project.dennis.transvision.Activities;

import android.app.ProgressDialog;
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

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;
    private AlertDialog.Builder mBuilder;
    private String mUserId, mUsername, mEmail, mHasMade;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        mBuilder = new AlertDialog.Builder(this);
        mLoginButton.setOnClickListener(this);

        getPrefUser();
        prefNotNull();
    }

    private void initView() {
        mEmailEditText = findViewById(R.id.edit_user_email);
        mPasswordEditText = findViewById(R.id.edit_user_password);
        mLoginButton = findViewById(R.id.button_login);
    }

    private void getPrefUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        mUserId = sharedPreferences.getString("user_id", "");
        mUsername = sharedPreferences.getString("username", "");
        mEmail = sharedPreferences.getString("email", "");
        mHasMade = sharedPreferences.getString("has_made_req", "");
        Log.d(TAG, "getPrefUser: "+mHasMade);
        mEmailEditText.setText(mEmail);
    }

    private void prefNotNull() {
        String emailString = mEmailEditText.getText().toString().trim();
        if (emailString.length() > 0 && mHasMade.equals("1")) {
            Intent intent = new Intent(this, WaitingActivity.class);
            startActivity(intent);
            finish();
        } else if (emailString.length() > 0 && mHasMade.equals("0")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginButton) {
            loginCheck();
        }
    }

    private void loginCheck() {
        final String emailString = mEmailEditText.getText().toString().trim();
        final String passwordString = mPasswordEditText.getText().toString().trim();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Mohon Tunggu...");
        mProgressDialog.show();

        if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
            mProgressDialog.dismiss();
            displayAlert("Masukkan email dan password dengan benar");
            return;
        }

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, ConfigLink.LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("failed")) {
                                displayAlert(jsonObject.getString("message"));
                            } else {
                                String userIdString = jsonObject.getString("user_id");
                                String usernameString = jsonObject.getString("username");
                                String emailString = jsonObject.getString("email");
                                String hasMadeReq = jsonObject.getString("has_made_req");
                                saveAttribute(userIdString, usernameString, emailString, hasMadeReq);
                                Log.d(TAG, "onResponse: "+hasMadeReq);
                                if (hasMadeReq.equals("1")) {
                                    Intent intentWaiting = new Intent(LoginActivity.this, WaitingActivity.class);
                                    startActivity(intentWaiting);
                                } else {
                                    Intent intentWaiting = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intentWaiting);
                                }
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.hide();
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
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void saveAttribute(String user_id, String username, String email, String hasMadeReq) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("has_made_req", hasMadeReq);
        editor.commit();
    }

    public void displayAlert(String message) {
        mBuilder.setMessage(message);
        mBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                mEmailEditText.setText("");
                mPasswordEditText.setText("");
                mEmailEditText.requestFocus();
            }
        });
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }
}
