package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.interfaces.IPlayerPresenter;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {
    private static PlayerPresenter sPlayerPresenter = null;
    private final XmPlayerManager mXmPlayerManager;
    private static final String TAG = "PlayerPresenter";
    private List<IPlayerViewCallback> mCallbackList = new ArrayList<>();
    private boolean isPlayListSet;
    private Track mCurrentTrack;
    private int mCurrentIndex;

    //单例化
    private PlayerPresenter(){
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getContext());
        //监听广告播放状态
        mXmPlayerManager.addAdsStatusListener(this);
        //监听播放器的状态变化
        mXmPlayerManager.addPlayerStatusListener(this);
    }
    public static PlayerPresenter getInstance(){
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class){
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }

    //接收来自DetailActivity的数据
    public void setTrackList(List<Track> list, int startIndex){
        //mXmPlayerManager.playList(list,startIndex);
        if(mXmPlayerManager != null) {
            mXmPlayerManager.setPlayList(list,startIndex);
            isPlayListSet = true;
            mCurrentTrack = list.get(startIndex);
            mCurrentIndex = startIndex;
        } else {
            LogUtil.d(TAG,"mPlayerManager is null");
        }
    }

    @Override
    public void play() {
        mXmPlayerManager.play();
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onPlayStart();
        }
    }

    @Override
    public void pause() {
        mXmPlayerManager.pause();
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onPlayPause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void play(int index) {

    }

    @Override
    public boolean isPlaying() {
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerViewCallback iPlayerViewCallback) {
        if (!mCallbackList.contains(iPlayerViewCallback)) {
            mCallbackList.add(iPlayerViewCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerViewCallback iPlayerViewCallback) {

    }


    //======================================广告播放状态 start=======================================
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG,"onStartGetAdsInfo...");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG,"onGetAdsInfo...");
    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG,"onAdsStartBuffering...");
    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG,"onAdsStopBuffering...");
    }

    @Override
    public void onStartPlayAds(Advertis ad, int position) {
        LogUtil.d(TAG,"onStartPlayAds...");
    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG,"onCompletePlayAds...");
    }

    @Override
    public void onError(int what, int extra) {
        LogUtil.d(TAG,"onError... what -- >"+ what +"extra -- > "+extra);
    }
    //======================================广告播放状态 end=========================================
    //
    //======================================播放器的状态 start=======================================
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart...");
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause...");
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop...");
    }

    //播放完成
    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG,"onSoundPlayComplete...");
    }

    //播放器准备完毕
    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG,"onSoundPrepared...");
    }

    /**
     *
     * @param lastModel：上一首model,可能为空
     * @param curModel：下一首model
     */
    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        LogUtil.d(TAG,"onSoundSwitch...");
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG,"onBufferingStart...");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG,"onBufferingStop...");
    }

    @Override
    public void onBufferProgress(int percent) {
        LogUtil.d(TAG,"onBufferProgress...percent --> "+percent);
    }

    @Override
    public void onPlayProgress(int currPos, int duration) {
        LogUtil.d(TAG,"onPlayProgress...currPos --> "+currPos+"  duration --> "+duration);
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError...e -- >" + e);
        return false;
    }
    //======================================播放器播放状态 end========================================


}
