package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.interfaces.IDetailPresenter;
import com.ianf.dailylisten.interfaces.IDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class DetailPresenter implements IDetailPresenter {
    private Album mAlbumByRecommend;
    private List<IDetailViewCallback> mCallbacks = new ArrayList<>();
    //单例设计模式
    private static DetailPresenter sDetailPresenter = null;
    public static DetailPresenter getInstance() {
        if (sDetailPresenter == null) {
            synchronized (DetailPresenter.class){
                if (sDetailPresenter == null) {
                    sDetailPresenter = new DetailPresenter();
                }
            }
        }
        return sDetailPresenter;
    }
    private DetailPresenter(){

    }

    public void setAlbumByRecommend(Album album) {
        mAlbumByRecommend = album;
    }
    //注册接口的时候，detailActivity也能获取到从Recommend来的album
    public void registerViewCallback(IDetailViewCallback callback){
        if (callback != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mAlbumByRecommend != null) {
                callback.getAlbumByRecommend(mAlbumByRecommend);
            }
        }
    }
    //注销注册接口，以免内存泄漏
    public void unRegisterViewCallback(IDetailViewCallback callback){
        if (callback != null ) {
            mCallbacks.remove(callback);
        }
    }


    @Override
    public void loadData() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void pullRefresh() {

    }
}
