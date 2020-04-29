package com.ianf.dailylisten.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.InnerHolder> {
    private List<Track> mTracks = new ArrayList<>();
    private int mCurrentIndex = 0;
    @NonNull
    @Override
    public PlayerListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_list, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.InnerHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    //保存数据，更新UI
    public void setData(List<Track> tracks) {
        mTracks.clear();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }

    //切歌时跟新index
    public void updateIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private final ImageView mPlayIconIv;
        private final TextView mTitleTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mPlayIconIv = itemView.findViewById(R.id.player_list_play_icon_iv);
            mTitleTv = itemView.findViewById(R.id.player_list_track_title_tv);
        }

        public void setData(int position) {
            //设置是否可见
            mPlayIconIv.setVisibility(mCurrentIndex==position ? View.VISIBLE: View.GONE);
            //设置歌名
            mTitleTv.setText(mTracks.get(position).getTrackTitle());
            //设置颜色
            mTitleTv.setTextColor(BaseApplication.getContext()
                    .getResources().getColor(mCurrentIndex==position?R.color.second_color:R.color.sub_text_title));
        }
    }
}
