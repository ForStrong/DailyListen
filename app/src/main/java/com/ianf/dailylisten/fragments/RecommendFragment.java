package com.ianf.dailylisten.fragments;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.AlbumRvAdapter;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.utils.Constants;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*create by IANDF in 2020/4/22
 *lastTime:
 *@description:
 *@usage:
*/

/*
    1. 通过sdk提供的接口获取数据获取数据
 */
public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mAlbum_rv;
    private AlbumRvAdapter mAlbumRvAdapter;


    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_recommend,container,false);
        initView();
        getCommendData();
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

    /**
    *description: 根据api 3.10.6 获取猜你喜欢数据
    *usage:
    */
    private void getCommendData() {
        Map<String, String> specificParams = new HashMap<>();
        specificParams.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT.toString());
        CommonRequest.getGuessLikeAlbum(specificParams, new IDataCallBack<GussLikeAlbumList>() {
            //回调接口已经是主线程了可以更新rv的UI
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        LogUtil.d(TAG,"size -> "+albumList.size());
                        //把请求回来的数据数据传给Rv_adapter
                        mAlbumRvAdapter.setData(albumList);

                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error ->"+ i +"errorMsg ->"+s);
            }
        });
    }

}
