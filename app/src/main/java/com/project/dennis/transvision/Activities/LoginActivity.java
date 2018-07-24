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
import com.project.dennis.transvision.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;

    private String url = ConfigLink.login;

    private AlertDialog.Builder builder;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);

        mEmailEditText = findViewById(R.id.edit_user_email);
        mPasswordEditText = findViewById(R.id.edit_user_password);

        builder = new AlertDialog.Builder(LoginActivity.this);

        if (session.loggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                        Toast.makeText(LoginActivity.this, "" + jsonObject.getString("user_id"), Toast.LENGTH_LONG).show();
                                        session.setLoggedIn(true);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        String id_user = jsonObject.getString("user_id");
                                        saveAttribute(id_user);
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
        });
    }

    private void saveAttribute(String user_id) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConfigLink.loginPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
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