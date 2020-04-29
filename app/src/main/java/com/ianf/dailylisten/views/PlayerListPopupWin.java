package com.ianf.dailylisten.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.PlayerListAdapter;
import com.ianf.dailylisten.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class PlayerListPopupWin extends PopupWindow {

    private final View mView;
    private TextView mPlayerListCloseTv;
    private PlayerListAdapter mPlayerListAdapter;
    private RecyclerView mPlayerListRv;

    public PlayerListPopupWin() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击外部消失
        setOutsideTouchable(true);
        //创建和设置视图
        mView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.player_list_popup_win, null);
        setContentView(mView);
        //设置动画
        setAnimationStyle(R.style.popupWinAnim);
        initView();
        initEvent();
    }

    private void initEvent() {
        //让它退出
        mPlayerListCloseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerListPopupWin.this.dismiss();
            }
        });


    }

    private void initView() {
        mPlayerListCloseTv = mView.findViewById(R.id.play_list_close_tv);

        //初始化Rv
        mPlayerListRv = mView.findViewById(R.id.play_list_rv);
        mPlayerListAdapter = new PlayerListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getContext());
        //给rv设置布局和adapter
        mPlayerListRv.setLayoutManager(layoutManager);
        mPlayerListRv.setAdapter(mPlayerListAdapter);
    }

    public void setTrackList(List<Track> tracks) {
        //TODO:给rv设置数据
        mPlayerListAdapter.setData(tracks);
    }

    public void updateIndex(int currentIndex) {
        mPlayerListAdapter.updateIndex(currentIndex);
    }
}
