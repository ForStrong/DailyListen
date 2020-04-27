package com.ianf.dailylisten.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public class PlayerActivity extends AppCompatActivity implements IPlayerViewCallback {

    private ImageView mPlayOrPauseIv;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //设置全屏隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //初始化Presenter
        mPlayerPresenter = PlayerPresenter.getInstance();
        //注册回调接口
        mPlayerPresenter.registerViewCallback(this);
        initView();
        initEvent();
        //进来就播放
        mPlayerPresenter.play();
    }

    private void initEvent() {
        mPlayOrPauseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.如果是播放状态就让它暂停，如果是暂停状态就让他播放
                if (mPlayerPresenter.isPlaying()){
                    mPlayerPresenter.pause();
                }else {
                    mPlayerPresenter.play();
                }

            }
        });
    }

    private void initView() {
        mPlayOrPauseIv = findViewById(R.id.play_or_pause_iv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mPlayerPresenter.unRegisterViewCallback(this);
        mPlayerPresenter = null;
    }

    //======================================IPlayerViewCallback start===============================
    @Override
    public void onPlayStart() {
        if (mPlayOrPauseIv != null) {
            mPlayOrPauseIv.setImageResource(R.mipmap.stop_normal);
        }
    }

    @Override
    public void onPlayPause() {
        if (mPlayOrPauseIv != null) {
            mPlayOrPauseIv.setImageResource(R.mipmap.play_normal);
        }
    }

    @Override
    public void onPlayStop() {
        if (mPlayOrPauseIv != null) {
            mPlayOrPauseIv.setImageResource(R.mipmap.play_normal);
        }
    }

    @Override
    public void onPlayNext() {

    }

    @Override
    public void onPlayPre() {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void onProcessChange(long currentProcess, int total) {

    }
    //======================================IPlayerViewCallback end===============================
}
