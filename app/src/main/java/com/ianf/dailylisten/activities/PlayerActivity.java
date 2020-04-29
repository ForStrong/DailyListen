package com.ianf.dailylisten.activities;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.PlayerViewPagerAdapter;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.views.PlayerListPopupWin;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.*;

/**
*create by IANDF in 2020/4/27
 *lastTime:
 *@description: ViewCallBack中的方法由于都是异步的，所以使用控件之前都要判空
 *@usage:
 *god bless my code
*/

public class PlayerActivity extends AppCompatActivity implements IPlayerViewCallback, ViewPager.OnPageChangeListener {
    private static final String TAG = "PlayerActivity";
    private ImageView mPlayOrPauseIv;
    private PlayerPresenter mPlayerPresenter;
    private TextView mDurationTv;
    private TextView mCurrentPositionTv;
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
    private TextView mTrackNameTv;
    private ViewPager mPlayerViewPager;
    private PlayerViewPagerAdapter mViewPagerAdapter;
    private ImageView mSwitchPlayModelIv;
    private XmPlayListControl.PlayMode mCurrentMode = PLAY_MODEL_LIST;
    private static HashMap<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> mPlayModeMap = new HashMap<>();
/*   设置播放器模式，mode取值为PlayMode中的下列之一：
     * 1.PLAY_MODEL_LIST列表播放
     * 2.PLAY_MODEL_SINGLE_LOOP 单曲循环播放
     * 3.PLAY_MODEL_LIST_LOOP列表循环
     * 4.PLAY_MODEL_RANDOM 随机播放
     */
    static {
        mPlayModeMap.put(PLAY_MODEL_LIST,PLAY_MODEL_SINGLE_LOOP);
        mPlayModeMap.put(PLAY_MODEL_SINGLE_LOOP,PLAY_MODEL_LIST_LOOP);
        mPlayModeMap.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM );
        mPlayModeMap.put(PLAY_MODEL_RANDOM,PLAY_MODEL_LIST);
    }

    private ImageView mPlayerListIv;
    private PlayerListPopupWin mPopupWin;
    private ValueAnimator mOutValueAnimator;
    private ValueAnimator mEnterValueAnimator;

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
        initPresenter();
        //初始化popupWin弹入，退出时背景透明度动画
        initAnimator();
    }

    private void initAnimator() {
        //popupWin弹入时动画
        mEnterValueAnimator = ValueAnimator.ofFloat(1.0f,0.6f);
        mEnterValueAnimator.setDuration(500);
        mEnterValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float)animation.getAnimatedValue();
                LogUtil.d(TAG,"alpha -- > " + alpha);
                //修改背景透明度
                updateWinAlpha(alpha);
            }
        });

        //popupWin退出时动画
        mOutValueAnimator = ValueAnimator.ofFloat(0.6f,1.0f);
        mOutValueAnimator.setDuration(500);
        mOutValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float)animation.getAnimatedValue();
                LogUtil.d(TAG,"alpha -- > " + alpha);
                //修改背景透明度
                updateWinAlpha(alpha);
            }
        });
    }

    /**
     * 修改背景透明度
     * @param alpha 背景透明度参数
     */
    private void updateWinAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    private void initPresenter() {
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
        //给ViewPager设置点击事件
        mPlayerViewPager.addOnPageChangeListener(this);

        mSwitchPlayModelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //告诉presenter让他设置点击事件
                mCurrentMode = mPlayModeMap.get(mCurrentMode);
                mPlayerPresenter.setPlayMode(mCurrentMode);
            }
        });
        //popWin弹出，设置背景透明度动画
        mPlayerListIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个方法的参考是全屏幕呢，所以这里的y值基本上不使用。
                mPopupWin.showAtLocation(v, Gravity.BOTTOM,0,0);
                mEnterValueAnimator.start();
            }
        });
        //popWin退出，设置背景透明度动画
        mPopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mOutValueAnimator.start();
            }
        });
        //popWin的ItemView点击事件回调
        mPopupWin.setOnPlayerListItemClickListener(new PlayerListPopupWin.OnPlayerListItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                mPlayerPresenter.play(position);
            }
        });


    }
    /*设置播放器模式，mode取值为PlayMode中的下列之一：
     * 1.PLAY_MODEL_LIST列表播放
     * 2.PLAY_MODEL_SINGLE_LOOP 单曲循环播放
     * 3.PLAY_MODEL_LIST_LOOP列表循环
     * 4.PLAY_MODEL_RANDOM 随机播放
     */
    private void switchUIByModel(XmPlayListControl.PlayMode currentMode) {
        int rId = R.drawable.selector_play_mode_list_order;
        switch (currentMode) {
            case PLAY_MODEL_LIST:
                rId = R.drawable.selector_play_mode_list_order;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                rId = R.drawable.selector_paly_mode_single_loop;
                break;
            case PLAY_MODEL_LIST_LOOP:
                rId = R.drawable.selector_paly_mode_list_order_looper;
                break;
            case PLAY_MODEL_RANDOM:
                rId = R.drawable.selector_paly_mode_random;
                break;
        }
        mSwitchPlayModelIv.setImageResource(rId);
    }

    private void initView() {
        mPlayOrPauseIv = findViewById(R.id.play_or_pause_iv);
        mDurationTv = findViewById(R.id.track_duration);
        mCurrentPositionTv = findViewById(R.id.current_position);
        mDurationSeekBar = findViewById(R.id.track_seek_bar);
        mPlayPreIv = findViewById(R.id.play_pre);
        mPlayNextIv = findViewById(R.id.play_next);
        mTrackTitle = findViewById(R.id.track_title);
        mTrackNameTv = findViewById(R.id.track_name);
        mPlayerViewPager = findViewById(R.id.player_view_pager);
        //给ViewPager添加适配器
        mViewPagerAdapter = new PlayerViewPagerAdapter();
        mPlayerViewPager.setAdapter(mViewPagerAdapter);
        mSwitchPlayModelIv = findViewById(R.id.player_mode_switch_iv);
        mPlayerListIv = findViewById(R.id.player_list);
        //初始化popupWin
        mPopupWin = new PlayerListPopupWin();
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
            mPlayOrPauseIv.setImageResource(R.drawable.selector_palyer_pause);
        }
    }

    @Override
    public void onPlayPause() {
        if (mPlayOrPauseIv != null) {
            mPlayOrPauseIv.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mPlayOrPauseIv != null) {
            mPlayOrPauseIv.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayNext() {
    }

    @Override
    public void onPlayPre() {

    }
    //设置播放器模式
    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {
        mCurrentMode = mode;
        switchUIByModel(mCurrentMode);
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
            if (mCurrentPositionTv != null) {
                mCurrentPositionTv.setText(currentProcessS);
            }
            if (mDurationSeekBar != null){
                mDurationSeekBar.setMax(total);
                mDurationSeekBar.setProgress(currentProcess);
            }
        }
    }

    @Override
    public void onTrackLoadedByDetail(Track track, int currentIndex) {
        //跟新控件UI
        mTrackTitle.setText(track.getTrackTitle());
        mTrackNameTv.setText(track.getAnnouncer().getNickname());
        mPopupWin.updateIndex(currentIndex);
    }


    //更新UI，ViewPager，TrackTitle ，TrackName ，PopupWin
    @Override
    public void onSoundSwitch(Track curTrack, int currentIndex) {
        if (mTrackTitle != null){
            mTrackTitle.setText(curTrack.getTrackTitle());
        }
        if (mTrackNameTv != null){
            mTrackNameTv.setText(curTrack.getAnnouncer().getNickname());
        }
        mPlayerViewPager.setCurrentItem(currentIndex);
        mPopupWin.updateIndex(currentIndex);
    }

    //11.获取当前trackList
    @Override
    public void onTrackListLoaded(List<Track> tracks) {
        //给ViewPager和popupWin设置tracks
        mViewPagerAdapter.setData(tracks);
        mPopupWin.setTrackList(tracks);
    }
    //======================================IPlayerViewCallback end===============================

    //======================================ViewPagerChange start===================================
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
