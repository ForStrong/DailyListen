package com.ianf.dailylisten.Presenters;

import android.widget.Toast;

import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.interfaces.IHistoryPresenterViewCallback;
import com.ianf.dailylisten.interfaces.IPlayerPresenter;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ianf.dailylisten.utils.Constants;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.utils.XimalayApi;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
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
    public static final int DEFAULT_PLAY_INDEX = 0;
    //PlayListSet是否设置好了
    private boolean isPlayListSet = false;
    //当前的Track
    private Track mCurrentTrack;
    //当前的Track在TrackList的位置
    private int mCurrentIndex;
    private int mCurrentProgressPosition = 0;
    private int mProgressDuration = 0;

    //单例化
    private PlayerPresenter(){
        //初始化mXmPlayerManager
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
        if (isPlayListSet) {
            mXmPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        mXmPlayerManager.pause();
    }

    @Override
    public void stop() {

    }

    @Override
    public void playNext() {
        if (mXmPlayerManager.hasNextSound())
            mXmPlayerManager.playNext();
    }

    @Override
    public void playPre() {
        if (mXmPlayerManager.hasPreSound())
            mXmPlayerManager.playPre();
    }
    //给ViewPagerAdapter传递数据
    @Override
    public void getPlayList() {
        for (IPlayerViewCallback callback : mCallbackList) {
            callback.onTrackListLoaded(mXmPlayerManager.getPlayList());
        }
    }

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode mode) {
        mXmPlayerManager.setPlayMode(mode);
        Constants.CURRENT_MODE = mode;
        for (IPlayerViewCallback callback : mCallbackList) {
            callback.onPlayModeChange(mode);
        }
    }
    //更新播放器为用户拖动进度条的位置
    @Override
    public void seekTo(int index) {
        mXmPlayerManager.seekTo(index);
    }

    @Override
    public boolean isPlaying() {
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void play(int position) {
        //playByIndex会调用onSoundSwitch
        mXmPlayerManager.play(position);
    }
    //判断是否设置过playList
    @Override
    public boolean hasPlayList() {
        return isPlayListSet;
    }

    @Override
    public void playByAlbumId(int albumId) {
        XimalayApi ximalayApi = XimalayApi.getXimalayApi();
        //根据AlbumId请求tracks,初始化播放器
        ximalayApi.getAlbumDetail(new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                List<Track> tracks = trackList.getTracks();
                if (tracks.size() > 0) {
                    mXmPlayerManager.setPlayList(tracks,DEFAULT_PLAY_INDEX);
                    isPlayListSet = true;
                    mCurrentTrack = tracks.get(DEFAULT_PLAY_INDEX);
                    mCurrentIndex = DEFAULT_PLAY_INDEX;
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BaseApplication.getContext(),"请求数据错误...",Toast.LENGTH_SHORT).show();
            }
        },albumId,1);
        //等播放器准备好了就播放了
    }

    @Override
    public void registerViewCallback(IPlayerViewCallback iPlayerViewCallback) {
        iPlayerViewCallback.onTrackLoadedByDetail(mCurrentTrack,mCurrentIndex);
        if (!mCallbackList.contains(iPlayerViewCallback)) {
            mCallbackList.add(iPlayerViewCallback);
        }
        //给ViewPagerAdapter传递数据
        getPlayList();
        //设置之前的模式
        //通知当前的节目
        iPlayerViewCallback.onSoundSwitch(mCurrentTrack,mCurrentIndex);
        iPlayerViewCallback.onProcessChange(mCurrentProgressPosition,mProgressDuration);
        handlePlayState(iPlayerViewCallback);
        for (IPlayerViewCallback callback : mCallbackList) {
            callback.onPlayModeChange(Constants.CURRENT_MODE);
        }

    }


    private void handlePlayState(IPlayerViewCallback iPlayerCallback) {
        int playerStatus = mXmPlayerManager.getPlayerStatus();
        //根据状态调用接口的方法
        if(PlayerConstants.STATE_STARTED == playerStatus) {
            iPlayerCallback.onPlayStart();
        } else {
            iPlayerCallback.onPlayPause();
        }

    }

    @Override
    public void unRegisterViewCallback(IPlayerViewCallback iPlayerViewCallback) {
        mCallbackList.remove(iPlayerViewCallback);
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

    //======================================播放器的状态 start=======================================
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart...");
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause...");
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop...");
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onPlayPause();
        }
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
        mXmPlayerManager.setPlayMode(Constants.CURRENT_MODE);

        mXmPlayerManager.play();
    }

    /**
     *切歌时调用
     * @param lastModel：上一首model,可能为空
     * @param curModel：当前model
     */
    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        mCurrentIndex = mXmPlayerManager.getCurrentIndex();
        LogUtil.d(TAG,"onSoundSwitch...");
        if (curModel instanceof Track){
            mCurrentTrack = (Track) curModel;
            HistoryPresenter.getInstance().addHistory(mCurrentTrack);
            for (IPlayerViewCallback callback : mCallbackList) {
                callback.onSoundSwitch(mCurrentTrack,mCurrentIndex);
            }
        }
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
        mProgressDuration = duration;
        mCurrentProgressPosition = currPos;
        //单位：毫秒
        LogUtil.d(TAG,"onPlayProgress...currPos --> "+currPos+"  duration --> "+duration);
        for (IPlayerViewCallback callback: mCallbackList) {
            callback.onProcessChange(currPos,duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError...e -- >" + e);
        return false;
    }
//======================================播放器播放状态 end========================================



}
