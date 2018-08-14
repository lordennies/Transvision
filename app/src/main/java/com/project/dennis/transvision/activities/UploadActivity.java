package com.project.dennis.transvision.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private static final int CAM_REQUEST = 100;
    private ImageView photoPreview;
    private Button buttonCamera;
    private TextView hasPickedImage;
    private Bitmap bitmap;
    private String convertImage, peminjamanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        builder = new AlertDialog.Builder(this);
        buttonCamera.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        peminjamanId = sharedPreferences.getString("peminjaman_id", "");
    }

    private void initView() {
        photoPreview = (ImageView) findViewById(R.id.photo_preview);
        buttonCamera = (Button) findViewById(R.id.button_camera);
        hasPickedImage = (TextView) findViewById(R.id.has_picked_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_camera:
                openCamera();
                break;
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                photoPreview.setImageBitmap(bitmap);
                hasPickedImage.setText("1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                uploadImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadImage() {
        if (hasPickedImage.getText().equals("0")) {
            displayAlert("Anda belum memilih foto");
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.show();

        imageToString();

        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, ConfigLink.UPLOAD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String responseString = jsonObject.getString("response");
                            Toast.makeText(UploadActivity.this, responseString, Toast.LENGTH_LONG).show();
                            photoPreview.setImageResource(R.drawable.no_image);
                            hasPickedImage.setText("0");
                            Intent trackerIntent = new Intent(UploadActivity.this, TrackerActivity.class);
                            startActivity(trackerIntent);
                            finishAffinity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UploadActivity.this, "Error JSON-nya nih!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadActivity.this, "Error Response nya nih!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(ConfigLink.PEMINJAMAN_ID, peminjamanId);
                params.put("image", convertImage);
                return params;
            }
        };
        MySingleton.getInstance(UploadActivity.this).addToRequestQueue(stringRequest);
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

    private void imageToString() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imgBytes = stream.toByteArray();
        convertImage = Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
}
