package com.ianf.dailylisten.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailRvAdapter extends RecyclerView.Adapter<DetailRvAdapter.InnerHolder> {
    private static final String TAG = "DetailRvAdapter";
    private List<Track> mTracks = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    //创造页面
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_rv_item,parent,false);
        return new InnerHolder(view);
    }
    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.setViewData(position);
        //给item设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(mTracks,position);
                }
                Toast.makeText(v.getContext(), "position" + v.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
    
    public void setData(List<Track> tracks){
        //清除以前数据
        mTracks.clear();
        //添加新数据
        mTracks.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }
    @SuppressLint("SimpleDateFormat")
    public class InnerHolder extends RecyclerView.ViewHolder {

        private final TextView mOrderTv;
        private final TextView mDetailTitleTv;
        private final TextView mDetailDurationTv;
        private final TextView mDetailPlayCountTv;
        private final TextView mUpdateTimeTv;

        private SimpleDateFormat mUpdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //初始化View
            mOrderTv = itemView.findViewById(R.id.order_text);
            mDetailTitleTv = itemView.findViewById(R.id.detail_item_title);
            mDetailDurationTv = itemView.findViewById(R.id.detail_item_duration);
            mDetailPlayCountTv = itemView.findViewById(R.id.detail_item_play_count);
            mUpdateTimeTv = itemView.findViewById(R.id.detail_item_update_time);
        }

        //给View设置数据
        @SuppressLint("SetTextI18n")
        public void setViewData(int position) {
            Track track = mTracks.get(position);
            mOrderTv.setText(position+"");
            if (track != null) {
                mDetailPlayCountTv.setText(track.getPlayCount()+"");
                mDetailTitleTv.setText(track.getTrackTitle());
                //获取播放时长（秒） * 1000 -> millisecond
                int durationMil = track.getDuration() * 1000;
                String duration = mDurationFormat.format(durationMil);
                mDetailDurationTv.setText(duration);
                String updateTime = mUpdateFormat.format(track.getUpdatedAt());
                mUpdateTimeTv.setText(updateTime);
            }
        }
    }

    //设置回调接口
    public void setItemClickListener(OnItemClickListener clickListener){
        mItemClickListener = clickListener;
    }
    //创建Item点击的回调接口
    public interface OnItemClickListener{
        void onItemClick(List<Track> tracks, int position);
    }
}
