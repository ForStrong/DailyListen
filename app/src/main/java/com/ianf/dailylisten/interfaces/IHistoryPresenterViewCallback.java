package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IHistoryPresenterViewCallback {
    /**
     * 历史内容加载结果.
     *
     * @param tracks
     */
    void onHistoriesLoaded(List<Track> tracks);

    /**
     *加载历史记录失败
     */
    void onHistoriesLoadedError();

    /**
     * 调用添加历史的时候 ，去通知UI结果
     *
     * @param isSuccess
     */
    void onHistoryAddResult(boolean isSuccess);


    /**
     * 删除历史的回调方法
     *
     * @param isSuccess
     */
    void onHistoryDeleteResult(boolean isSuccess);

    /**
     * 删除历史内容回调。
     */
    void onCleanedHistory(boolean isSuccess);
}
