package com.project.dennis.transvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;

    private TextView mStatusTextView;

    private String url = "http://192.168.56.1/dennis/transvision/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.edit_user_email);
        mPasswordEditText = findViewById(R.id.edit_user_password);
        mStatusTextView = findViewById(R.id.status);

        mStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, EditorActivity.class));
            }
        });

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Dipencet", Toast.LENGTH_SHORT).show();

                String emailString = mEmailEditText.getText().toString().trim();
                String passwordString = mPasswordEditText.getText().toString().trim();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    mStatusTextView.setText(response.getString("status"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mStatusTextView.setText("Something went wrong...");
                                error.printStackTrace();
                            }
                        });

                MySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
