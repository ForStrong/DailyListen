package com.ianf.dailylisten.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseActivity;
import com.ianf.dailylisten.interfaces.IDetailViewCallback;
import com.ianf.dailylisten.views.RoundTransform;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IDetailViewCallback {

    private ImageView mSmallCoverIv;
    private TextView mAlbumTitleTv;
    private TextView mAlbumAuthorTv;
    private DetailPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //设置全屏隐藏状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //初始化View
        initView();
        //初始化presenter
        mPresenter = DetailPresenter.getInstance();
        //注册回调接口
        mPresenter.registerViewCallback(this);
    }

    private void initView() {
        mSmallCoverIv = findViewById(R.id.iv_small_cover);
        mAlbumTitleTv = findViewById(R.id.tv_album_title);
        mAlbumAuthorTv = findViewById(R.id.tv_album_author);
    }
    //获取到从recommendFragment来的album，给控件添加内容
    @Override
    public void getAlbumByRecommend(Album albumByRecommend) {
        Picasso.with(this).load(albumByRecommend.getCoverUrlSmall()).transform(new RoundTransform()).into(mSmallCoverIv);
        mAlbumAuthorTv.setText((albumByRecommend.getAnnouncer().getNickname()));
        mAlbumTitleTv.setText(albumByRecommend.getAlbumTitle());
    }

    @Override
    public void onDetailListLoaded(List<Track> trackList) {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {

    }
    //销毁时注册接口
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegisterViewCallback(this);
    }
}
