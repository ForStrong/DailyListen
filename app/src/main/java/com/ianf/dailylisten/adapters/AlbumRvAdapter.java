package com.ianf.dailylisten.adapters;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.views.RoundTransform;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumRvAdapter extends RecyclerView.Adapter<AlbumRvAdapter.InnerHolder> {
    private List<Album> mAlbums = new ArrayList<>();
    private OnAlbumItemClickListener mOnAlbumItemClickListener;
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建itemView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_album_rv_item, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        holder.itemView.setTag(position);
        //给item设置数据
        holder.setData(mAlbums.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAlbumItemClickListener != null) {
                    mOnAlbumItemClickListener.albumItemClickListener((int)v.getTag(),mAlbums.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums == null? 0:mAlbums.size();
    }

    public void setData(List<Album> albums){
        //跟新mAlbums数据
        if (mAlbums != null) {
            mAlbums.clear();
            mAlbums.addAll(albums);
        }
        //更新UI
        notifyDataSetChanged();
    }
    public void setAlbumItemClickListener(OnAlbumItemClickListener listener){
        mOnAlbumItemClickListener = listener;
    }

    public interface OnAlbumItemClickListener{
        void albumItemClickListener(int tag, Album album);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private ImageView mAlbumCoverIv;
        private TextView mAlbumTitleTv;
        private TextView mAlbumDesTv;
        private TextView mAlbumPlayCountTv;
        private TextView mAlbumContentCountTv;
        //初始化所有控件
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //专辑封面
            mAlbumCoverIv = itemView.findViewById(R.id.album_cover);
            //title
            mAlbumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            mAlbumDesTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            mAlbumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            //专辑内容数量
            mAlbumContentCountTv = itemView.findViewById(R.id.album_content_size);
        }
        //给各个控件设置数据
        @SuppressLint("SetTextI18n")
        public void setData(Album album) {
            mAlbumTitleTv.setText(album.getAlbumTitle());
            mAlbumDesTv.setText(album.getAlbumIntro());
            mAlbumContentCountTv.setText(album.getIncludeTrackCount()+"");
            mAlbumPlayCountTv.setText(album.getPlayCount()+"");
            //把图片URL通过Picasso添加到imageView中
            String coverUrlLarge = album.getCoverUrlLarge();
            if (!TextUtils.isEmpty(coverUrlLarge)){
                Picasso.with(itemView.getContext()).load(coverUrlLarge).transform(new RoundTransform()).into(mAlbumCoverIv);
            }else {
                mAlbumCoverIv.setImageResource(R.mipmap.ximalay_logo);
            }
        }
    }
}
