package com.project.dennis.transvision.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.dennis.transvision.activities.DetailActivity;
import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.R;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.PeminjamanHolder> {

    private ArrayList<Peminjaman> list;
    private Context context;

    public PeminjamanAdapter(ArrayList<Peminjaman> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PeminjamanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_peminjaman, null);
        return new PeminjamanHolder(view);
    }

    @Override
    public void onBindViewHolder(PeminjamanHolder holder, int position) {
        Peminjaman peminjaman = list.get(position);
        holder.tujuan.setText(peminjaman.getTujuan());
        holder.tglPemakaian.setText(peminjaman.getTglPemakaian());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /** Kelas PeminjamanViewHolder */
    class PeminjamanHolder extends RecyclerView.ViewHolder {

        private TextView tujuan, tglPemakaian;

        private PeminjamanHolder(View itemView) {
            super(itemView);

            tujuan = itemView.findViewById(R.id.tujuan);
            tglPemakaian = itemView.findViewById(R.id.tgl_pemakaian);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putParcelableArrayListExtra("list", list);
                    intent.putExtra("position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}
