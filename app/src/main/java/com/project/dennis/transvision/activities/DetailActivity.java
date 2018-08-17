package com.project.dennis.transvision.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.dennis.transvision.R;
import com.project.dennis.transvision.models.Peminjaman;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView tujuanTextView;
    private TextView keperluanTextView;
    private TextView jumPenumpangTextView;
    private TextView tglPemakaianTextView;

    List<Peminjaman> list;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();

        list = getIntent().getParcelableArrayListExtra("list");
        position = getIntent().getIntExtra("position",-1);

        // Menampilkan detail peminjaman tertentu
        tujuanTextView.setText(list.get(position).getTujuan());
        keperluanTextView.setText(list.get(position).getKeperluan());
        jumPenumpangTextView.setText(list.get(position).getJumPenumpang());
        tglPemakaianTextView.setText(list.get(position).getTglPemakaian());
    }

    private void initView() {
        tujuanTextView = (TextView) findViewById(R.id.tujuan_text_view);
        keperluanTextView = (TextView) findViewById(R.id.keperluan_text_view);
        jumPenumpangTextView = (TextView) findViewById(R.id.jum_penumpang_text_view);
        tglPemakaianTextView = (TextView) findViewById(R.id.tgl_pemakaian_text_view);
    }
}
