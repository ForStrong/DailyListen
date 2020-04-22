package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendViewCallback {
    //加载猜你喜欢数据完成
    void onRecommendListLoaded(List<Album> albumList);
}
