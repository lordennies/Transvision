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
import android.util.Log;
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
import com.project.dennis.transvision.models.UploadResponse;
import com.project.dennis.transvision.retrofit.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog dialog;
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
        peminjamanId = sharedPreferences.getString("peminjamanId", "");
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
            displayAlert("Anda belum mengambil gambar");
            return;
        }
        // Tampilkan progress dialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Mohon Tunggu...");
        dialog.show();
        // ubah gambar ke format string
        imageToString();
        // Upload gambar ke server
        Call<UploadResponse> call = Client.getInstanceRetrofit().upload(convertImage, peminjamanId);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, retrofit2.Response<UploadResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    UploadResponse uploadResponse = response.body();
                    if (uploadResponse.getStatus().equals("success")) {
                        startActivity(new Intent(UploadActivity.this, TrackerActivity.class));
                        finish();
                        Toast.makeText(UploadActivity.this, "Gambar ditambahkan", Toast.LENGTH_SHORT).show();
                    } else if (uploadResponse.getStatus().equals("failed")) {
                        Toast.makeText(UploadActivity.this, "Gambar gagal ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UploadActivity.this, "Gambar gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
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
