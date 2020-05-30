package com.ianf.dailylisten.interfaces;

import com.ianf.dailylisten.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {
    //加载数据
    void loadData(Boolean isLoadMore);

    Album getCurrentAlbum();
}
