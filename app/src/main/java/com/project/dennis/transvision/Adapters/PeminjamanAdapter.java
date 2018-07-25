package com.project.dennis.transvision.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.dennis.transvision.Models.Peminjaman;
import com.project.dennis.transvision.R;

import java.util.List;

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder> {

    private Context mContext;
    private List<Peminjaman> peminjamanList;

    public PeminjamanAdapter(Context mContext, List<Peminjaman> peminjamanList) {
        this.mContext = mContext;
        this.peminjamanList = peminjamanList;
    }

    @Override
    public PeminjamanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, null);
        return new PeminjamanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeminjamanViewHolder holder, int position) {
        Peminjaman peminjaman = peminjamanList.get(position);

        holder.tujuan.setText(peminjaman.getTujuan());
        holder.tglPemakaian.setText(peminjaman.getTglPemakaian());
    }

    @Override
    public int getItemCount() {
        return peminjamanList.size();
    }

    class PeminjamanViewHolder extends RecyclerView.ViewHolder {

        TextView tujuan, tglPemakaian;

        public PeminjamanViewHolder(View itemView) {
            super(itemView);

            tujuan = itemView.findViewById(R.id.tujuan);
            tglPemakaian = itemView.findViewById(R.id.tgl_pemakaian);
        }
    }
}
