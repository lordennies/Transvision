package com.project.dennis.transvision.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.dennis.transvision.Data.ConfigLink;
import com.project.dennis.transvision.Models.Peminjaman;
import com.project.dennis.transvision.Adapters.PeminjamanAdapter;
import com.project.dennis.transvision.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PeminjamanAdapter.ListItemClickListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private PeminjamanAdapter mAdapter;
    private List<Peminjaman> peminjamanList;
    private Toast mToast;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_peminjaman);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {

        } else {
            loadPeminjaman();
        }
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigLink.PEMINJAMAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray peminjamanArray = new JSONArray(response);
                            for (int i = 0; i < peminjamanArray.length(); i++) {
                                JSONObject peminjamanObject = peminjamanArray.getJSONObject(i);

                                String tujuan = peminjamanObject.getString(ConfigLink.TUJUAN);
                                String tglPemakaian = peminjamanObject.getString(ConfigLink.TGL_PEMAKAIAN);

                                Peminjaman peminjaman = new Peminjaman(tujuan, tglPemakaian);
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
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
