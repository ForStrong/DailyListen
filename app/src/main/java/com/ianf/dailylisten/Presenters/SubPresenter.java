package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.data.ISubDaoCallback;
import com.ianf.dailylisten.data.SubscriptionDao;
import com.ianf.dailylisten.interfaces.ISubPresenter;
import com.ianf.dailylisten.interfaces.ISubViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class SubPresenter implements ISubPresenter, ISubDaoCallback {
    private static final String TAG = "SubPresenter";
    private SubscriptionDao mSubscriptionDao = SubscriptionDao.getInstance();
    private List<ISubViewCallback> mISubViewCallbacks = new ArrayList<>();

    private SubPresenter() {

    }
    public static SubPresenter getInstance(){
        return InnerHolder.INSTANCE;
    }
    private static class InnerHolder{
        private static final SubPresenter INSTANCE = new SubPresenter();
    }

    @Override
    public void addSubscription(Album album) {
        mSubscriptionDao.addAlbum(album);
    }

    @Override
    public void deleteSubscription(Album album) {
        mSubscriptionDao.delAlbum(album);
    }

    @Override
    public void getSubscriptionList() {
        mSubscriptionDao.getAlbums();
    }

    @Override
    public void isSub(Album album) {
        mSubscriptionDao.isSub(album);
    }

    @Override
    public void registerViewCallback(ISubViewCallback iSubViewCallback) {
        if (iSubViewCallback!=null && !mISubViewCallbacks.contains(iSubViewCallback))
            mISubViewCallbacks.add(iSubViewCallback);
        mSubscriptionDao.setCallback(this);
    }

    @Override
    public void unRegisterViewCallback(ISubViewCallback iSubViewCallback) {
        mISubViewCallbacks.remove(iSubViewCallback);
    }

    //=====================ISubDaoCallback start====================================================
    @Override
    public void onAddResult(boolean isSuccess) {
        for (ISubViewCallback iSubViewCallback : mISubViewCallbacks) {
            iSubViewCallback.onAddResult(isSuccess);
        }
    }

    @Override
    public void onDelResult(boolean isSuccess) {
        for (ISubViewCallback iSubViewCallback : mISubViewCallbacks) {
            iSubViewCallback.onDeleteResult(isSuccess);
        }
    }

    @Override
    public void onSubListLoaded(List<Album> albums) {
        LogUtil.d(TAG,"albums size -> "+albums.size());
        for (ISubViewCallback iSubViewCallback : mISubViewCallbacks) {
            iSubViewCallback.onSubscriptionsLoaded(albums);
        }
    }

    @Override
    public void onSubListError() {
        for (ISubViewCallback iSubViewCallback : mISubViewCallbacks) {
            iSubViewCallback.onSubLoadedError();
        }
    }

    @Override
    public void isASub(boolean isSub) {
        LogUtil.d(TAG,"isSub -> " + isSub);
        for (ISubViewCallback iSubViewCallback : mISubViewCallbacks) {
            iSubViewCallback.isSub(isSub);
        }
    }
    //=====================ISubDaoCallback start====================================================
}
