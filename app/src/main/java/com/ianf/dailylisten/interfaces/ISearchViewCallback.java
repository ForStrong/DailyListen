package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

public interface ISearchViewCallback {
    void onHotWordsLoaded(List<HotWord> hotWordList);
    void onDataLoaded(List<Album> result,boolean isLoadMore);
    void onGuessWordsLoaded(List<QueryResult> keyWordList);
    /**
     *description:数据加载失败
     */
    void onError();
}
