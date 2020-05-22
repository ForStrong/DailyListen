package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IDetailViewCallback {
    //页面跳转时，从Recommend获取到了album
    void getAlbumByRecommend(Album albumByRecommend);
    //加载专辑ID获取专辑下的声音列表完成
    void onDetailListLoaded(List<Track> trackList,boolean isLoadMore);
    //数据为空
    void onEmpty();
    //网络错误
    void onNetworkError();
    //正在加载
    void onLoading();
}
