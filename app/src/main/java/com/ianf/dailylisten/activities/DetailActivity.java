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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.DetailRvAdapter;
import com.ianf.dailylisten.base.BaseActivity;
import com.ianf.dailylisten.interfaces.IDetailViewCallback;
import com.ianf.dailylisten.views.RoundTransform;
import com.ianf.dailylisten.views.UILoader;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IDetailViewCallback, UILoader.OnRetryClickListener,DetailRvAdapter.OnItemClickListener  {

    private ImageView mSmallCoverIv;
    private TextView mAlbumTitleTv;
    private TextView mAlbumAuthorTv;
    private DetailPresenter mPresenter;
    private Album mAlbum;
    private int mCurrentPage = 1;
    private RecyclerView mDetailRv;
    private DetailRvAdapter mDetailRvAdapter;
    private FrameLayout mDetailContainer;
    private UILoader mUILoader;
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
    }

    //初始化顶部UI
    private void initBaseView() {
        mSmallCoverIv = findViewById(R.id.iv_small_cover);
        mAlbumTitleTv = findViewById(R.id.tv_album_title);
        mAlbumAuthorTv = findViewById(R.id.tv_album_author);
    }

    //初始化activity_detail_rv，在网络成功加载完数据时
    private View initSuccessView(ViewGroup container) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_detail_rv, container, false);
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
        mPresenter = DetailPresenter.getInstance();
        //注册回调接口,会调用callback.getAlbumByRecommend(mAlbumByRecommend)拿到Album
        mPresenter.registerViewCallback(this);

        //加载数据
        mPresenter.loadData((int) mAlbum.getId(),mCurrentPage);
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
    public void onDetailListLoaded(List<Track> trackList) {
        mDetailRvAdapter.setData(trackList);
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
        mPresenter.unRegisterViewCallback(this);
    }

    @Override
    public void retryLoadData() {
        mPresenter.loadData((int) mAlbum.getId(),mCurrentPage);
    }

    @Override
    public void onItemClick() {
        //TODO:调转到播放页面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}
