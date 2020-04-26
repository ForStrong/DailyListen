package com.ianf.dailylisten.interfaces;

import com.ianf.dailylisten.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
/*
    1.播放
    2.暂定
    3.停止
    4.下一首
    5.上一首
    6.获取专辑列表
    7.切换播放状态：随机or顺序or...
    8.按进度条切换播放
 */

public interface IPlayerPresenter extends IBasePresenter<IPlayerViewCallback> {
    //1.播放
    void play();

    //2.暂定
    void pause();

    //3.停止
    void stop();

    //4.下一首
    void playNext();

    //5.上一首
    void playPre();

    //6.获取专辑列表
    void getPlayList();

    /**
     * 7.切换播放状态：随机or顺序or...
    *description:
     * 设置播放器模式，mode取值为PlayMode中的下列之一：
     * PLAY_MODEL_SINGLE单曲播放
     * PLAY_MODEL_SINGLE_LOOP 单曲循环播放
     * PLAY_MODEL_LIST列表播放
     * PLAY_MODEL_LIST_LOOP列表循环
     * PLAY_MODEL_RANDOM 随机播放
    */
    void setPlayMode(XmPlayListControl.PlayMode mode);

    //8.按进度条切换播放
    void play(int index);

}
