package com.ianf.dailylisten.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.DetailRvAdapter;
import com.ianf.dailylisten.base.BaseActivity;
import com.ianf.dailylisten.interfaces.IDetailViewCallback;
import com.ianf.dailylisten.interfaces.IPlayerViewCallback;
import com.ianf.dailylisten.views.RoundTransform;
import com.ianf.dailylisten.views.UILoader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class DetailActivity extends BaseActivity implements IDetailViewCallback, UILoader.OnRetryClickListener, DetailRvAdapter.OnItemClickListener, IPlayerViewCallback {

    private ImageView mSmallCoverIv;
    private TextView mAlbumTitleTv;
    private TextView mAlbumAuthorTv;
    private DetailPresenter mDetailPresenter;
    private Album mAlbum;
    private int mCurrentPage = 1;
    private RecyclerView mDetailRv;
    private DetailRvAdapter mDetailRvAdapter;
    private FrameLayout mDetailContainer;
    private UILoader mUILoader;
    private ImageView mPlayControlIv;
    private TextView mPlayControlTv;
    private PlayerPresenter mPlayerPresenter;
    private List<Track> mTracks;
    private final int DEFAULT_POSITION = 0;
    //    private Track mCurrentTrack;
    private String mCurrentTrackTitle;
    private RefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //设置全屏隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //初始化UILoader
        if (mUILoader == null) {
            mUILoader = new UILoader(this) {
                @Override
                public View getSuccessView(ViewGroup container) {
                    return initSuccessView(container);
                }
            };
        }

        //注册网络不佳时重新加载的接口
        mUILoader.setOnRetryListener(this);
        //初始化DetailContainer，并清空View,然后添加UILoader
        mDetailContainer = findViewById(R.id.detail_container);
        mDetailContainer.removeAllViews();
        mDetailContainer.addView(mUILoader);

        //初始化顶部UI
        initBaseView();
        //初始化Presenter
        initPresenter();
        //初始化顶部UI点击事件
        initBaseEvent();

    }

    private void initBaseEvent() {
        mPlayControlIv.setImageResource(mPlayerPresenter.isPlaying() ? R.drawable.selector_play_control_pause : R.drawable.selector_play_control_play);
        if (!mPlayerPresenter.isPlaying()) {
            mPlayControlTv.setText("点击播放");
        } else {

            mPlayControlTv.setText(mCurrentTrackTitle);
        }

        mPlayControlIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果设置过PlayList实现点击播放和暂停的功能，没有设置过，默认播放第一首
                if (mPlayerPresenter.hasPlayList()) {
                    if (mPlayerPresenter.isPlaying()) {
                        mPlayerPresenter.pause();
                    } else {
                        mPlayerPresenter.play();
                    }
                } else {
                    mPlayerPresenter.setTrackList(mTracks, DEFAULT_POSITION);
                }

            }
        });
    }

    //初始化顶部UI
    private void initBaseView() {
        mSmallCoverIv = findViewById(R.id.iv_small_cover);
        mAlbumTitleTv = findViewById(R.id.tv_album_title);
        mAlbumAuthorTv = findViewById(R.id.tv_album_author);
        mPlayControlIv = findViewById(R.id.detail_play_control);
        mPlayControlTv = findViewById(R.id.play_control_tv);
        //实现跑马灯效果必备
        mPlayControlTv.setSelected(true);
    }

    //初始化activity_detail_rv，在网络成功加载完数据时
    private View initSuccessView(ViewGroup container) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_detail_rv, container, false);
        mRefreshLayout = view.findViewById(R.id.detailRefreshLayout);
        //禁止下拉刷新
        mRefreshLayout.setEnableRefresh(false);
        //recyclerview回弹的效果
        mRefreshLayout.setEnableOverScrollBounce(true);
        mRefreshLayout.setEnableOverScrollDrag(true);
        mRefreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
                //完成加载更多的功能
                mDetailPresenter.loadMore();
                refreshLayout1.finishLoadMore(2000);
            }
        );
        mDetailRv = view.findViewById(R.id.detail_rv);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailRv.setLayoutManager(layoutManager);
        //设置adapter
        mDetailRvAdapter = new DetailRvAdapter();
        //给Adapter设置itemView点击事件
        mDetailRvAdapter.setItemClickListener(this);
        //Rv设置Adapter
        mDetailRv.setAdapter(mDetailRvAdapter);
        return view;
    }

    private void initPresenter() {
        //初始化presenter
        mDetailPresenter = DetailPresenter.getInstance();
        //注册回调接口,会调用callback.getAlbumByRecommend(mAlbumByRecommend)拿到Album
        mDetailPresenter.registerViewCallback(this);
        //加载数据
        if (mAlbum != null) {
            mDetailPresenter.loadData((int) mAlbum.getId(), mCurrentPage);
        }

        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewCallback(this);
    }

    //获取到从recommendFragment来的album，给控件添加内容
    @Override
    public void getAlbumByRecommend(Album albumByRecommend) {
        mAlbum = albumByRecommend;
        Picasso.with(this).load(albumByRecommend.getCoverUrlSmall()).transform(new RoundTransform()).into(mSmallCoverIv);
        mAlbumAuthorTv.setText((albumByRecommend.getAnnouncer().getNickname()));
        mAlbumTitleTv.setText(albumByRecommend.getAlbumTitle());

    }

    //加载数据完成presenter会回调这个接口,通知RvAdapter更新数据和UI
    @Override
    public void onDetailListLoaded(List<Track> trackList,boolean isLoadMore) {
        if (!isLoadMore) {
            this.mTracks = trackList;
        }else {
            if (trackList.size() > 0){
                mTracks.addAll(trackList);
            }else {
                Toast.makeText(this, "无更多多数据", Toast.LENGTH_SHORT).show();
            }
            mRefreshLayout.finishLoadMore();
        }
        mDetailRvAdapter.setData(mTracks);
        mUILoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onEmpty() {
        mUILoader.upDataUIStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onNetworkError() {
        mUILoader.upDataUIStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onLoading() {
        mUILoader.upDataUIStatus(UILoader.UIStatus.LOADING);
    }

    //销毁时注册接口
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.unRegisterViewCallback(this);
        mPlayerPresenter.unRegisterViewCallback(this);
    }

    @Override
    public void retryLoadData() {
        mDetailPresenter.loadData((int) mAlbum.getId(), mCurrentPage);
    }

    @Override
    public void onItemClick(List<Track> tracks, int position) {
        //给播放页面传递数据
        PlayerPresenter.getInstance().setTrackList(tracks, position);
        //调转到播放页面
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    //==================================playerPresenterViewCallback start===============================
    @Override
    public void onPlayStart() {
        //改变imageControl和textControl UI
        mPlayControlIv.setImageResource(R.drawable.selector_play_control_pause);
        mPlayControlTv.setText(mCurrentTrackTitle);

    }

    @Override
    public void onPlayPause() {
        //改变imageControl和textControl UI
        mPlayControlIv.setImageResource(R.drawable.selector_play_control_play);
        mPlayControlTv.setText("点击播放");
    }

    @Override
    public void onPlayStop() {
        //改变imageControl和textControl UI
        mPlayControlIv.setImageResource(R.drawable.selector_play_control_play);
        mPlayControlTv.setText("点击播放");
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
        if (track != null)
            mCurrentTrackTitle = track.getTrackTitle();
    }

    //切歌的时候调用
    @Override
    public void onSoundSwitch(Track curTrack, int currentIndex) {
        if (curTrack != null) {
            mCurrentTrackTitle = curTrack.getTrackTitle();
            mPlayControlTv.setText(mCurrentTrackTitle);
        }
    }

    @Override
    public void onTrackListLoaded(List<Track> tracks) {

    }
//==================================playerPresenterViewCallback end===============================
}
