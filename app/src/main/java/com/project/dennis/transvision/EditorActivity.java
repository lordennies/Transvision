package com.project.dennis.transvision;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {

    String action, tujuanString, keperluanString, jumPenumpangString, tglPemakaianString;

    private EditText mTujuanEditText, mKeperluanEditText, mJumPenumpangEditText, mTglPemakaianEditText;

    private String url = "http://192.168.100.5/lordennies/transvision-cls/api/pinjam";

    private boolean mPeminjamanHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPeminjamanHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTujuanEditText = findViewById(R.id.edit_tujuan);
        mKeperluanEditText = findViewById(R.id.edit_keperluan);
        mJumPenumpangEditText = findViewById(R.id.edit_jum_penumpang);
        mTglPemakaianEditText = findViewById(R.id.edit_tgl_pemakaian);

        mTujuanEditText.setOnTouchListener(mTouchListener);
        mKeperluanEditText.setOnTouchListener(mTouchListener);
        mJumPenumpangEditText.setOnTouchListener(mTouchListener);
        mTglPemakaianEditText.setOnTouchListener(mTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    private void simpanPeminjaman() {
        action = "create";
        tujuanString = mTujuanEditText.getText().toString().trim();
        keperluanString = mKeperluanEditText.getText().toString().trim();
        jumPenumpangString = mJumPenumpangEditText.getText().toString().trim();
        tglPemakaianString = mTglPemakaianEditText.getText().toString().trim();

        if (TextUtils.isEmpty(tujuanString) || TextUtils.isEmpty(keperluanString) ||
                TextUtils.isEmpty(jumPenumpangString) || TextUtils.isEmpty(tglPemakaianString)) {
            showUncompletedFormDialog();
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
                            if (status.equals("success")) {
                                Toast.makeText(EditorActivity.this, "Permohonan ditambahkan",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditorActivity.this, "Penambahan gagal",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("tujuan", tujuanString);
                params.put("keperluan", keperluanString);
                params.put("jum_penumpang", jumPenumpangString);
                params.put("tgl_pemakaian", tglPemakaianString);
                return params;
            }
        };
        MySingleton.getInstance(EditorActivity.this).addToRequestQueue(stringRequest);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                simpanPeminjaman();
                return true;
            case android.R.id.home:
                if (!mPeminjamanHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mPeminjamanHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUncompletedFormDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Mohon isi semua form");
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

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batalkan peminjaman dan keluar?");
        builder.setPositiveButton("Keluar", discardButtonClickListener);
        builder.setNegativeButton("Lanjutkan", new DialogInterface.OnClickListener() {
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
