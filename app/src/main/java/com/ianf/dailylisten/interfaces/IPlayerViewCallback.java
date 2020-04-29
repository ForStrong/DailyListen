package com.ianf.dailylisten.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
*create by IANDF in 2020/4/26
 *lastTime:
 *@description:
 * 1.开始播放
 * 2.暂停播放
 * 3.停止播放
 * 4.进度条改变
 * 5.下一首
 * 6.上一首
 * 7.播放模式改变
 * 8.进度条改变
 * 9.从detailActivity获取到Track
 * 10.切歌时调用
 * 11.获取当前trackList
 *god bless my code
*/
public interface IPlayerViewCallback {
    /**
     * 1.开始播放
     */
    void onPlayStart();

    /**
     * 2.暂停播放
     */
    void onPlayPause();

    /**
     * 3.停止播放
     */
    void onPlayStop();

    /**
     * 5.下一首
     */
    void onPlayNext();

    /**
     * 6.上一首
     */
    void onPlayPre();

    /**
     * 7.播放模式改变
     * @param mode:播放模式
     */
    void onPlayModeChange(XmPlayListControl.PlayMode mode);

    /**
     * 8.进度条改变
     */
    void onProcessChange(int currentProcess,int total);
    /**
     * 9.从detailActivity获取到Track
     */
    void onTrackLoadedByDetail(Track track, int currentIndex);

    /**
     * 10.切歌时调用
     * @param curTrack 切换后的track
     * @param currentIndex
     */
    void onSoundSwitch(Track curTrack, int currentIndex);

    /**
     * 获取当前trackList
     */
    void onTrackListLoaded(List<Track> tracks);
}
