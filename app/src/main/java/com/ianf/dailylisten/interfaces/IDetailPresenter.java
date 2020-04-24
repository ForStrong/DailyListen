package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

public interface IDetailPresenter {
    //加载数据
    void loadData();
    //上拉加载更多
    void loadMore();
    //下拉刷新
    void pullRefresh();
}
