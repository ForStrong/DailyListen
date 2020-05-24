package com.ianf.dailylisten;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.Presenters.RecommendPresenter;
import com.ianf.dailylisten.activities.PlayerActivity;
import com.ianf.dailylisten.activities.SearchActivity;
import com.ianf.dailylisten.adapters.IndicatorAdapter;
import com.ianf.dailylisten.adapters.MainViewPagerAdapter;
import com.ianf.dailylisten.base.BaseActivity;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ianf.dailylisten.views.RoundTransform;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;


public class MainActivity extends BaseActivity implements IPlayerViewCallback {
    private static final String TAG = "MainActivity";
    private MagicIndicator magicIndicator;
    private ImageView mPlayControlIv;
    private ImageView mPlayTrackCoverIv;
    private TextView mPlayTrackTitle;
    private TextView mPlayTrackAuthor;
    private PlayerPresenter mPlayerPresenter;
    private Album mDefaultAlbum;
    private ConstraintLayout mPlayControlLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
        initEvent();
    }

    private void initPresenter() {
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewCallback(this);
    }

    private void initEvent() {
        mPlayControlIv.setOnClickListener(v -> {
            //如果第一次进入则播放默认歌曲，如果不是则显示歌曲内容
            if (!mPlayerPresenter.hasPlayList()){
                //没有播放列表，播放默认专辑第一个音频
                playFirstRecommendAudio();
            }else {
                if (mPlayerPresenter.isPlaying()){
                    mPlayerPresenter.pause();
                }else {
                    mPlayerPresenter.play();
                }
            }
        });

        mPlayControlLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            startActivity(intent);
        });

    }

    private void playFirstRecommendAudio() {
        mDefaultAlbum = RecommendPresenter.getInstance().getCurrentAlbum();
        long albumId = mDefaultAlbum.getId();
        mPlayerPresenter.playByAlbumId((int) albumId);
    }

    private void initView() {
        magicIndicator = findViewById(R.id.magic_indicator);
        ViewPager viewPager = findViewById(R.id.content_pager);
        //init indicator
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自定义Indicator适配器
        IndicatorAdapter adapter = new IndicatorAdapter(this,viewPager);
        //给CommonNavigator设置适配器
        commonNavigator.setAdapter(adapter);
        //根据控件宽度自动调节Item的位置,平分宽度
        commonNavigator.setAdjustMode(true);
        //给Indicator设置导航栏
        magicIndicator.setNavigator(commonNavigator);
        //init viewPager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //自定义ViewPager适配器
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(fragmentManager);
        //给ViewPager设置适配器
        viewPager.setAdapter(viewPagerAdapter);
        //bind viewPager and indicator
        ViewPagerHelper.bind(magicIndicator,viewPager);
        RelativeLayout searchLayout = findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        //底部播放器控件初始化
        mPlayControlIv = findViewById(R.id.main_play_control);
        mPlayTrackCoverIv = findViewById(R.id.main_track_cover);
        mPlayTrackTitle = findViewById(R.id.main_track_title);
        mPlayTrackTitle.setSelected(true);
        mPlayTrackAuthor = findViewById(R.id.mian_track_author);
        mPlayControlLayout = findViewById(R.id.main_play_control_item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerPresenter.unRegisterViewCallback(this);
    }


    //===========PlayerViewCallback start===========================================================
    @Override
    public void onPlayStart() {
        setUIByPlayState(true);
    }

    @Override
    public void onPlayPause() {
        setUIByPlayState(false);
    }

    @Override
    public void onPlayStop() {
        setUIByPlayState(false);
    }
    private void setUIByPlayState(boolean isPlay){
        mPlayControlIv.setImageResource(isPlay?R.drawable.selector_palyer_pause:R.drawable.selector_player_play);
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
    public void onProcessChange(int currentProcess, int total) {

    }

    @Override
    public void onTrackLoadedByDetail(Track track, int currentIndex) {

    }
    //播放歌曲必定会调用这个方法
    @Override
    public void onSoundSwitch(Track curTrack, int currentIndex) {
        if (curTrack != null) {
            String trackTitle = curTrack.getTrackTitle();
            String nickname = curTrack.getAnnouncer().getNickname();
            String coverUrlMiddle = curTrack.getCoverUrlMiddle();
            mPlayTrackAuthor.setText(nickname);
            mPlayTrackTitle.setText(trackTitle);
            Picasso.with(this).load(coverUrlMiddle).transform(new RoundTransform()).into(mPlayTrackCoverIv);
        }
    }

    @Override
    public void onTrackListLoaded(List<Track> tracks) {

    }
    //===========PlayerViewCallback end=============================================================


}
