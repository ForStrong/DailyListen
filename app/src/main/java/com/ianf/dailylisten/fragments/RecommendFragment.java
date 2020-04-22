package com.ianf.dailylisten.fragments;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ianf.dailylisten.Presenters.RecommendPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.AlbumRvAdapter;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.interfaces.IRecommendViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
*create by IANDF in 2020/4/22
 *lastTime:
 *@description:
 *@usage:
*/

/*
    1. 通过sdk提供的接口获取数据获取数据
 */
public class RecommendFragment extends BaseFragment implements IRecommendViewCallback {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mAlbum_rv;
    private AlbumRvAdapter mAlbumRvAdapter;
    private RecommendPresenter mPresenter;


    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_recommend,container,false);
        initView();
        //创建Presenter
        mPresenter = RecommendPresenter.getInstance();
        //注册回调接口
        mPresenter.registerCallback(this);
        //加载数据
        mPresenter.loadData();
        return mRootView;
    }

    private void initView() {
        //初始化rv
        mAlbum_rv = mRootView.findViewById(R.id.album_rv);
        //设置recyclerView的布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAlbum_rv.setLayoutManager(linearLayoutManager);
        //初始化RvAdapter
        mAlbumRvAdapter = new AlbumRvAdapter();
        //Rv设置adapter
        mAlbum_rv.setAdapter(mAlbumRvAdapter);
    }



    @Override
    public void onRecommendListLoaded(List<Album> albumList) {
        mAlbumRvAdapter.setData(albumList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销回调接口
        mPresenter.unRegisterCallback(this);
    }
}
