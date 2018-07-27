package com.project.dennis.transvision.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.dennis.transvision.Data.ConfigLink;
import com.project.dennis.transvision.Models.Peminjaman;
import com.project.dennis.transvision.Adapters.PeminjamanAdapter;
import com.project.dennis.transvision.MySingleton;
import com.project.dennis.transvision.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements PeminjamanAdapter.ListItemClickListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private PeminjamanAdapter mAdapter;
    private List<Peminjaman> peminjamanList;
    private Toast mToast;
    private FloatingActionButton fab;
    private TextView mHasMadeReq;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        fab.setOnClickListener(this);
        mToast = null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getAttributeUser();

        getPrefUser();
        loadPeminjaman();
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.rv_peminjaman);
        mHasMadeReq = findViewById(R.id.has_made_req);
    }

    private void getPrefUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        String stringHasMadeReq = sharedPreferences.getString("has_made_req", "");
        mHasMadeReq.setText(stringHasMadeReq);
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        }
    }

    private void loadPeminjaman() {
        peminjamanList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigLink.PEMINJAMAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray peminjamanArray = new JSONArray(response);
                            for (int i = 0; i < peminjamanArray.length(); i++) {
                                JSONObject peminjamanObject = peminjamanArray.getJSONObject(i);

                                String tujuan = peminjamanObject.getString(ConfigLink.TUJUAN);
                                String keperluan = peminjamanObject.getString(ConfigLink.KEPERLUAN);
                                String jumPenumpang = peminjamanObject.getString(ConfigLink.JUM_PENUMPANG);
                                String tglPemakaian = peminjamanObject.getString(ConfigLink.TGL_PEMAKAIAN);

                                Peminjaman peminjaman = new Peminjaman(tujuan, keperluan, jumPenumpang, tglPemakaian);
                                peminjamanList.add(peminjaman);
                            }
                            mAdapter = new PeminjamanAdapter(MainActivity.this, peminjamanList, MainActivity.this);
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(ConfigLink.USER_ID, userId);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void getAttributeUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
    }
}
