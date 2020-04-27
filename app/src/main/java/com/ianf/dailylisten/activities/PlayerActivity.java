package com.ianf.dailylisten.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
/**
*create by IANDF in 2020/4/27
 *lastTime:
 *@description: ViewCallBack中的方法由于都是异步的，所以使用控件之前都要判空
 *@usage:
 *god bless my code
*/

public class PlayerActivity extends AppCompatActivity implements IPlayerViewCallback {

    private ImageView mPlayOrPauseIv;
    private PlayerPresenter mPlayerPresenter;
    private TextView mDurationTv;
    private TextView mCurrentPosition;
    //格式化时间
    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private SeekBar mDurationSeekBar;
    //记录用户触摸更新的进度条进度
    private int mCurrentPos = 0;
    //判断当前时间是不是用户在改变进度条
    private boolean isFromUser = false;
    private ImageView mPlayPreIv;
    private ImageView mPlayNextIv;
    private TextView mTrackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //设置全屏隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //先初始化UI再写PlayerPresenter
        initView();
        initEvent();
        //初始化Presenter
        mPlayerPresenter = PlayerPresenter.getInstance();
        //注册回调接口
        mPlayerPresenter.registerViewCallback(this);
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

        mDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //是用户触摸的，更新当前进度
                if (fromUser){
                    mCurrentPos = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isFromUser = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户停止触摸进度条，改变播放器的播放状态和更新isFromUser
                isFromUser = false;
                mPlayerPresenter.seekTo(mCurrentPos);
            }
        });

        mPlayPreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerPresenter.playPre();
            }
        });

        mPlayNextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerPresenter.playNext();
            }
        });
    }

    private void initView() {
        mPlayOrPauseIv = findViewById(R.id.play_or_pause_iv);
        mDurationTv = findViewById(R.id.track_duration);
        mCurrentPosition = findViewById(R.id.current_position);
        mDurationSeekBar = findViewById(R.id.track_seek_bar);
        mPlayPreIv = findViewById(R.id.play_pre);
        mPlayNextIv = findViewById(R.id.play_next);
        mTrackTitle = findViewById(R.id.track_title);
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
        if (mTrackTitle != null) {
//            mTrackTitle.setText();
        }
    }

    @Override
    public void onPlayPre() {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {

    }
    //如果用户没触摸进度条，自动跟新进度条和时间
    @Override
    public void onProcessChange(int currentProcess, int total) {
        if (!isFromUser) {
            String durationS;
            String currentProcessS;
            if (total > 1000 * 60 * 60) {
                durationS = mHourFormat.format(total);
                currentProcessS = mHourFormat.format(currentProcess);
            }else{
                durationS = mMinFormat.format(total);
                currentProcessS = mMinFormat.format(currentProcess);
            }
            if (mDurationTv != null) {
                mDurationTv.setText(durationS);
            }
            if (mCurrentPosition != null) {
                mCurrentPosition.setText(currentProcessS);
            }
            if (mDurationSeekBar != null){
                mDurationSeekBar.setMax(total);
                mDurationSeekBar.setProgress(currentProcess);
            }
        }
    }

    @Override
    public void onTrackLoadedByDetail(Track track) {
        //跟新控件UI
        mTrackTitle.setText(track.getTrackTitle());
    }

    @Override
    public void onSoundSwitch(Track curTrack) {
        if (mTrackTitle != null){
            mTrackTitle.setText(curTrack.getTrackTitle());
        }
    }
    //======================================IPlayerViewCallback end===============================
}
