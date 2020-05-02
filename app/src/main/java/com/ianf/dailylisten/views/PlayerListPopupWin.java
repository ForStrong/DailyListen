package com.ianf.dailylisten.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.PlayerListAdapter;
import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerListPopupWin extends PopupWindow {

    private final View mView;
    private TextView mPlayerListCloseTv;
    private PlayerListAdapter mPlayerListAdapter;
    private RecyclerView mPlayerListRv;
    private ImageView mPlayModeIv;
    private ConstraintLayout mPlayModeLayout;
    private TextView mPlayModeTv;
    private OnPlayModeChangeListener mPlayModeListener;

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

        mPlayModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变播放模式
                mPlayModeListener.onPlayModeChangeClick();
                //改变UI
                switchUIByModel(Constants.CURRENT_MODE);
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

        mPlayModeIv = mView.findViewById(R.id.play_list_play_mode_iv);
        mPlayModeTv = mView.findViewById(R.id.play_list_play_mode_tv);
        mPlayModeLayout = mView.findViewById(R.id.play_list_play_mode_layout);

    }

    private void switchUIByModel(XmPlayListControl.PlayMode currentMode) {
        int rIvId = R.drawable.selector_play_mode_list_order;
        int rTvId = R.string.play_mode_order_text;
        switch (currentMode) {
            case PLAY_MODEL_LIST:
                rIvId = R.drawable.selector_play_mode_list_order;
                rTvId = R.string.play_mode_order_text;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                rIvId = R.drawable.selector_paly_mode_single_loop;
                rTvId = R.string.play_mode_single_play_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                rIvId = R.drawable.selector_paly_mode_list_order_looper;
                rTvId = R.string.play_mode_list_play_text;
                break;
            case PLAY_MODEL_RANDOM:
                rIvId = R.drawable.selector_paly_mode_random;
                rTvId = R.string.play_mode_random_text;
                break;
        }
        mPlayModeIv.setImageResource(rIvId);
        mPlayModeTv.setText(rTvId);
    }
    //数据从presenter来
    public void setTrackList(List<Track> tracks) {
        //给rv设置数据
        mPlayerListAdapter.setData(tracks);
    }

    public void updateIndex(int currentIndex) {
        mPlayerListAdapter.updateIndex(currentIndex);
        mPlayerListRv.scrollToPosition(currentIndex);
    }


    //在playerActivity注册，点击之后抛给playerActivity处理
    public void setOnPlayerListItemClickListener(OnPlayerListItemClickListener listItemClickListener){
        mPlayerListAdapter.setItemClickListener(listItemClickListener);
    }

    public interface OnPlayerListItemClickListener{
        void onItemClick(int position);
    }

    //在playerActivity注册，点击之后抛给playerActivity处理
    public void setOnPlayModeChangeListener(OnPlayModeChangeListener listener){
        this.mPlayModeListener = listener;
    }

    public interface OnPlayModeChangeListener{
        void onPlayModeChangeClick();
    }
}
