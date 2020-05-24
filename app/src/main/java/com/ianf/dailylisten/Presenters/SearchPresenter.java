package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.interfaces.ISearchPresenter;
import com.ianf.dailylisten.interfaces.ISearchViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.utils.XimalayApi;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {
    private List<ISearchViewCallback> mCallbackList = new ArrayList<>();
    private static final String TAG = "SearchPresenter";
    private XimalayApi mXimalayApi;
    private List<HotWord> mHotWords = new ArrayList<>();

    private SearchPresenter() {
        mXimalayApi = XimalayApi.getXimalayApi();
    }

    private static class SearchPresenterHolder {
        static final SearchPresenter INSTANCE = new SearchPresenter();
    }

    public static SearchPresenter getInstance() {
        return SearchPresenterHolder.INSTANCE;
    }

    @Override
    public void getHotWords() {
        if (mHotWords.size() > 0) {
            for (ISearchViewCallback iSearchViewCallback : mCallbackList) {
                iSearchViewCallback.onHotWordsLoaded(mHotWords);
            }
        }else {
            mXimalayApi.getHotWords(new IDataCallBack<HotWordList>() {
                @Override
                public void onSuccess(HotWordList hotWordList) {
                    mHotWords = hotWordList.getHotWordList();
                    for (ISearchViewCallback iSearchViewCallback : mCallbackList) {
                        iSearchViewCallback.onHotWordsLoaded(mHotWords);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    @Override
    public void getDataByKeyword(String keyword) {

    }

    @Override
    public void getGuessWords(String guessWord) {
        mXimalayApi.getSuggestWord(guessWord, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    for (ISearchViewCallback iSearchViewCallback : mCallbackList) {
                        iSearchViewCallback.onGuessWordsLoaded(keyWordList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    @Override
    public void registerViewCallback(ISearchViewCallback iSearchViewCallback) {
        if (iSearchViewCallback != null && !mCallbackList.contains(iSearchViewCallback)) {
            mCallbackList.add(iSearchViewCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISearchViewCallback iSearchViewCallback) {
        mCallbackList.remove(iSearchViewCallback);
    }
}
