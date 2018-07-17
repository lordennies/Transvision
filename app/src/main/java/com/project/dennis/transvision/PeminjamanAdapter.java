package com.project.dennis.transvision;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    /*private static final String TAG = PeminjamanAdapter.class.getSimpleName();

    private int mNumberItems;

    public PeminjamanAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }*/

//    @Override
//    public PeminjamanViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Context context = viewGroup.getContext();
//        int layoutIdForListItem = R.layout.list_item;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false;
//
//        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
//        NumberViewHolder viewHolder = new NumberViewHolder(view);
//
//        return viewHolder;
//    }

//    @Override
//    public void onBindViewHolder(NumberViewHolder holder, int position) {
//        Log.d(TAG, "#" + position);
//        holder.bind(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNumberItems;
//    }

//    class NumberViewHolder extends RecyclerView.ViewHolder {
//
//        // Will display the position in the list, ie 0 through getItemCount() - 1
//        TextView listItemNumberView;
//
//        /**
//         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
//         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
//         * onClick method below.
//         * @param itemView The View that you inflated in
//         *                 {@link PeminjamanAdapter#onCreateViewHolder(ViewGroup, int)}
//         */
//        public NumberViewHolder(View itemView) {
//            super(itemView);
//
//            listItemNumberView = itemView.findViewById(R.id.tujuan);
//        }
//
//        /**
//         * A method we wrote for convenience. This method will take an integer as input and
//         * use that integer to display the appropriate text within a list item.
//         * @param listIndex Position of the item in the list
//         */
//        void bind(int listIndex) {
//            listItemNumberView.setText(String.valueOf(listIndex));
//        }
//    }
}
