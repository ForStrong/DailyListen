package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.interfaces.ISearchPresenter;
import com.ianf.dailylisten.interfaces.ISearchViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.utils.XimalayApi;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {
    private List<ISearchViewCallback> mCallbackList = new ArrayList<>();
    private static final String TAG = "SearchPresenter";

    private SearchPresenter() {

    }

    private static class SearchPresenterHolder {
        static final SearchPresenter INSTANCE = new SearchPresenter();
    }

    public static SearchPresenter getInstance() {
        return SearchPresenterHolder.INSTANCE;
    }

    @Override
    public void getHotWords() {
        XimalayApi.getXimalayApi().getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                List<HotWord> hotWords = hotWordList.getHotWordList();
                for (ISearchViewCallback iSearchViewCallback : mCallbackList) {
                    iSearchViewCallback.onHotWordsLoaded(hotWords);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void getDataByKeyword(String keyword) {

    }

    @Override
    public void getGuessWords(String guessWord) {

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
