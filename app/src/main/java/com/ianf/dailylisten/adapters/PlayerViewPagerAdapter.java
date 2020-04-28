package com.ianf.dailylisten.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.views.RoundTransform;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayerViewPagerAdapter extends PagerAdapter {
    private List<Track> mTracks = new ArrayList<>();
    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.player_view_pager_item, container, false);
        container.addView(itemView);
        Track track = mTracks.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();
        ImageView imageView = itemView.findViewById(R.id.player_view_pager_iv);
        Picasso.with(container.getContext()).load(coverUrlLarge).transform(new RoundTransform()).into(imageView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<Track> tracks) {
        //清空旧数据，添加型数据，更新UI
        mTracks.clear();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }
}
