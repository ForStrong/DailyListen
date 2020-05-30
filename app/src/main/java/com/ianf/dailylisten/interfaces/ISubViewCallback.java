package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface ISubViewCallback {
    /**
     * 调用添加的时候 ，去通知UI结果
     *
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);


    /**
     * 删除订阅的回调方法
     *
     * @param isSuccess
     */
    void onDeleteResult(boolean isSuccess);


    /**
     * 订阅专辑加载的结果回调方法
     *
     * @param albums
     */
    void onSubscriptionsLoaded(List<Album> albums);
    /**
    *description:通知前端是否订阅了
    *usage:
    */
    void isSub(boolean isSub);

    /**
     *加载订阅专辑失败
     */
    void onSubLoadedError();
}
