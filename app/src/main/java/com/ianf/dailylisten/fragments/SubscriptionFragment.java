package com.ianf.dailylisten.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.Presenters.SubPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.activities.DetailActivity;
import com.ianf.dailylisten.adapters.AlbumRvAdapter;
import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.interfaces.ISubViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionFragment extends BaseFragment implements ISubViewCallback {
    private static final String TAG = "SubscriptionFragment";
    private SubPresenter mSubPresenter;
    private AlbumRvAdapter mAlbumRvAdapter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_subscription,container,false);
        initView(inflater,rootView);
        initPresenter();
        return rootView;
    }

    private void initPresenter() {
        mSubPresenter = SubPresenter.getInstance();
        mSubPresenter.registerViewCallback(this);
        //请求数据，初始化recyclerView
        mSubPresenter.getSubscriptionList();
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
        }
    }

    private void initView(LayoutInflater inflater,View rootView) {
        FrameLayout subContainer = rootView.findViewById(R.id.sub_container);
        mUiLoader = new UILoader(Objects.requireNonNull(getContext())) {
            @Override
            public View getSuccessView(ViewGroup container) {
                return initSuccessView(inflater,container);
            }
        };
        if (mUiLoader.getParent() instanceof ViewGroup){
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        subContainer.addView(mUiLoader);
    }

    private View initSuccessView(LayoutInflater inflater,ViewGroup container) {
        //初始化recyclerView
        View successView = inflater.inflate(R.layout.sub_ui_loader_success_layout, container, false);
        RecyclerView recyclerView = successView.findViewById(R.id.sub_albumRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAlbumRvAdapter = new AlbumRvAdapter();
        recyclerView.setAdapter(mAlbumRvAdapter);
        mAlbumRvAdapter.setAlbumItemClickListener((position, album) -> {
            //处理Item点击事件
            Toast.makeText(getContext(), "position ->"+position, Toast.LENGTH_SHORT).show();
            DetailPresenter.getInstance().setAlbumByRecommend(album);
            LogUtil.d(TAG,"album coverURL"+album.getCoverUrlLarge());
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            startActivity(intent);
        });
        return successView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubPresenter.unRegisterViewCallback(this);
    }

    //====================ISubViewCallback  start=======================================================
    @Override
    public void onAddResult(boolean isSuccess) {
        mSubPresenter.getSubscriptionList();
    }

    @Override
    public void onDeleteResult(boolean isSuccess) {
        mSubPresenter.getSubscriptionList();
    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {
        LogUtil.d(TAG,"albums size ->"+albums.size());
        if (albums.size() > 0){
            mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
            mAlbumRvAdapter.setData(albums);
            mAlbumRvAdapter.notifyDataSetChanged();
        }else {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.EMPTY);
        }
    }

    @Override
    public void isSub(boolean isSub) {

    }
//====================ISubViewCallback  end=======================================================
}
