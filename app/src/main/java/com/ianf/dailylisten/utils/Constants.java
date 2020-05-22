package com.ianf.dailylisten.utils;

import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
*create by IANDF in 2020/4/22
 *lastTime:
 *@description: 存储各种常量
 *@usage:
*/
public class Constants {
    // 猜你喜欢内容的条数
    public static int COUNT_RECOMMEND = 50;
    public static int COUNT_TRACKS_PAGE_SIZE = 50;

    //默认列表请求数量
    public static int COUNT_DEFAULT = 50;

    //热词的数量
    public static int COUNT_HOT_WORD = 10;

    public static XmPlayListControl.PlayMode CURRENT_MODE = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
}
