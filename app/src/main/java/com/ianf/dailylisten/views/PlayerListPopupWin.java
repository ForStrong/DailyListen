package com.ianf.dailylisten.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseApplication;

public class PlayerListPopupWin extends PopupWindow {

    public PlayerListPopupWin() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击外部消失
        setOutsideTouchable(true);
        //创建和设置视图
        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.player_list_popup_win, null);
        setContentView(view);
        //设置动画
        setAnimationStyle(R.style.popupWinAnim);

    }
}
