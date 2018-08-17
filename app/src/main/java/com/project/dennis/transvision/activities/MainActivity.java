package com.project.dennis.transvision.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.adapters.PeminjamanAdapter;
import com.project.dennis.transvision.MySingleton;
import com.project.dennis.transvision.R;
import com.project.dennis.transvision.retrofit.ApiService;
import com.project.dennis.transvision.retrofit.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String userId;
    private TextView mHasMadeReq;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private PeminjamanAdapter mAdapter;
    private ArrayList<Peminjaman> peminjamanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inisialisasi view
        initView();
        // Set layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        fab.setOnClickListener(this);

        getPrefUser();
        getPeminjaman();
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        mHasMadeReq = findViewById(R.id.has_made_req);
        recyclerView = findViewById(R.id.rv_peminjaman);
    }

    private void getPrefUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        String stringHasMadeReq = sharedPreferences.getString("has_made_req", "");
        userId = sharedPreferences.getString("user_id", "");
        mHasMadeReq.setText(stringHasMadeReq);
    }

    private void getPeminjaman() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getPeminjaman(userId).enqueue(new Callback<ArrayList<Peminjaman>>() {
            @Override
            public void onResponse(Call<ArrayList<Peminjaman>> call, retrofit2.Response<ArrayList<Peminjaman>> response) {
                if (response.isSuccessful()) {
                    peminjamanList = response.body();
                    mAdapter = new PeminjamanAdapter(peminjamanList, MainActivity.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Peminjaman>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        }
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

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigLink.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
