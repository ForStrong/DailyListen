package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.interfaces.IPlayerPresenter;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerPresenter implements IPlayerPresenter {
    private static PlayerPresenter sPlayerPresenter = null;
    private final XmPlayerManager mXmPlayerManager;

    //单例化
    private PlayerPresenter(){
        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getContext());
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
        mXmPlayerManager.playList(list,startIndex);
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

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
    public void registerViewCallback(IPlayerViewCallback iPlayerViewCallback) {

    }

    @Override
    public void unRegisterViewCallback(IPlayerViewCallback iPlayerViewCallback) {

    }
}
