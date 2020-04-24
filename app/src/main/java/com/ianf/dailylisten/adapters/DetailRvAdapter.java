package com.ianf.dailylisten.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class DetailRvAdapter extends RecyclerView.Adapter<DetailRvAdapter.InnerHolder> {
    private List<Track> mTracks = new ArrayList<>();
    //创造页面
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_rv_item,parent,false);
        return new InnerHolder(view);
    }
    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }
    public void setData(List<Track> tracks){
        //清除以前数据
        mTracks.clear();
        //添加新数据
        mTracks.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }
    public class InnerHolder extends RecyclerView.ViewHolder {

        private final TextView mOrderTv;
        private final TextView mDetailTitleTv;
        private final TextView mDetialDurationTv;
        private final TextView mDetailPlayCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mOrderTv = itemView.findViewById(R.id.order_text);
            mDetailTitleTv = itemView.findViewById(R.id.detail_item_title);
            mDetialDurationTv = itemView.findViewById(R.id.detail_item_duration);
            mDetailPlayCountTv = itemView.findViewById(R.id.detail_item_play_count);
        }


        public void setData(int position) {

        }
    }
}
