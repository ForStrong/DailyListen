package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.data.HistoryDao;
import com.ianf.dailylisten.data.IHistoryDaoViewCallback;
import com.ianf.dailylisten.interfaces.IHistoryPresenter;
import com.ianf.dailylisten.interfaces.IHistoryPresenterViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements IHistoryDaoViewCallback, IHistoryPresenter {
    private static final String TAG = "HistoryPresenter";
    private HistoryDao mHistoryDao = HistoryDao.getInstance();
    private List<IHistoryPresenterViewCallback> mPresenterViewCallbacks = new ArrayList<>();
    private HistoryPresenter() {
        mHistoryDao.setCallback(this);
    }
    private static class InnerHolder {
        static final HistoryPresenter HISTORY_PRESENTER = new HistoryPresenter();
    }
    public static HistoryPresenter getInstance(){
        return InnerHolder.HISTORY_PRESENTER;
    }

//=========================IHistoryPresenter start==================================================
    @Override
    public void listHistories() {
        mHistoryDao.listHistories();
    }

    @Override
    public void addHistory(Track track) {
        mHistoryDao.addHistory(track);
    }

    @Override
    public void delHistory(Track track) {
        mHistoryDao.delHistory(track);
    }

    @Override
    public void cleanHistories() {
        mHistoryDao.clearHistory();
    }

    @Override
    public void registerViewCallback(IHistoryPresenterViewCallback iHistoryPresenterViewCallback) {
        if (iHistoryPresenterViewCallback != null
                &&!mPresenterViewCallbacks.contains(iHistoryPresenterViewCallback)) {
            mPresenterViewCallbacks.add(iHistoryPresenterViewCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IHistoryPresenterViewCallback iHistoryPresenterViewCallback) {
        mPresenterViewCallbacks.remove(iHistoryPresenterViewCallback);
    }

//=========================IHistoryPresenter end====================================================

//=========================IHistoryDaoViewCallback start============================================
    @Override
    public void onHistoryAdd(boolean isSuccess) {
        LogUtil.d(TAG,"onHistoryAdd isSuccess ->" + isSuccess);
        for (IHistoryPresenterViewCallback presenterViewCallback : mPresenterViewCallbacks) {
            presenterViewCallback.onHistoryAddResult(isSuccess);
        }
    }

    @Override
    public void onHistoryDel(boolean isSuccess) {
        for (IHistoryPresenterViewCallback presenterViewCallback : mPresenterViewCallbacks) {
            presenterViewCallback.onHistoryDeleteResult(isSuccess);
        }
    }

    @Override
    public void onHistoriesLoaded(List<Track> tracks) {
        LogUtil.d(TAG,"tracks size -> " + tracks.size());
        for (IHistoryPresenterViewCallback presenterViewCallback : mPresenterViewCallbacks) {
            presenterViewCallback.onHistoriesLoaded(tracks);
        }
    }

    @Override
    public void onHistoriesError() {
        for (IHistoryPresenterViewCallback presenterViewCallback : mPresenterViewCallbacks) {
            presenterViewCallback.onHistoriesLoadedError();
        }
    }

    @Override
    public void onHistoriesClean(boolean isSuccess) {
        for (IHistoryPresenterViewCallback presenterViewCallback : mPresenterViewCallbacks) {
            presenterViewCallback.onCleanedHistory(isSuccess);
        }
    }
//=========================IHistoryDaoViewCallback end==============================================

}
