package com.ianf.dailylisten.fragments;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.Presenters.RecommendPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.activities.DetailActivity;
import com.ianf.dailylisten.adapters.AlbumRvAdapter;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.interfaces.IRecommendViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.views.UILoader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;
import java.util.Objects;

/**
*create by IANDF in 2020/4/22
 *lastTime:
 *@description: 推荐页面 mvp模式，在里面实现回调接口并让Presenter注册接口，这样model层代码改变时就调用接口中方法
 *@usage:
*/
public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener,AlbumRvAdapter.OnAlbumItemClickListener {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mAlbum_rv;
    private AlbumRvAdapter mAlbumRvAdapter;
    private RecommendPresenter mPresenter;
    private UILoader mUiLoader;


    @Override
    protected View onSubViewLoad(final LayoutInflater inflater, final ViewGroup container) {
        mUiLoader = new UILoader(Objects.requireNonNull(getContext())) {
            @Override
            public View getSuccessView(ViewGroup container) {
                return initView(inflater,container);
            }
        };
        //创建Presenter
        mPresenter = RecommendPresenter.getInstance();
        //注册回调接口
        mPresenter.registerViewCallback(this);
        //注册网络错误时重新获取数据接口
        mUiLoader.setOnRetryListener(this);
        //加载数据
        mPresenter.loadData();
        //android不允许多次绑定
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        return mUiLoader;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_recommend,container,false);
        //初始化rv
        mAlbum_rv = mRootView.findViewById(R.id.album_rv);
        RefreshLayout refreshLayout = mRootView.findViewById(R.id.recommendRefreshLayout);
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setReboundDuration(2000);
        //设置recyclerView的布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAlbum_rv.setLayoutManager(linearLayoutManager);
        //初始化RvAdapter
        mAlbumRvAdapter = new AlbumRvAdapter();
        //给RvItem设置回调接口
        mAlbumRvAdapter.setAlbumItemClickListener(this);
        //Rv设置adapter
        mAlbum_rv.setAdapter(mAlbumRvAdapter);
        return mRootView;
    }

    @Override
    public void retryLoadData() {
        mPresenter.loadData();
    }



    @Override
    public void onRecommendListLoaded(List<Album> albumList) {
        mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
        mAlbumRvAdapter.setData(albumList);
    }

    @Override
    public void onEmpty() {
        mUiLoader.upDataUIStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.upDataUIStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onLoading() {
        mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销回调接口
        mPresenter.unRegisterViewCallback(this);
    }

    @Override
    public void albumItemClickListener(int tag, Album album) {
        DetailPresenter.getInstance().setAlbumByRecommend(album);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
