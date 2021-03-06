package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.interfaces.IRecommendPresenter;
import com.ianf.dailylisten.interfaces.IRecommendViewCallback;
import com.ianf.dailylisten.utils.Constants;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.utils.XimalayApi;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter{
    private static RecommendPresenter RECOMMEND_PRESENTER = null;
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private String TAG = "RecommendPresenter";
    private List<Album> mCurrentAlbums = null;
    private final XimalayApi mXimalayApi;
    private int currentPage = 1;
    //懒汉式单例设计模式
    public static RecommendPresenter getInstance(){
        if (RECOMMEND_PRESENTER == null) {
            synchronized (RecommendPresenter.class){
                if (RECOMMEND_PRESENTER == null) {
                    RECOMMEND_PRESENTER = new RecommendPresenter();
                }
            }
        }
        return RECOMMEND_PRESENTER;
    }

    //私有化构造方法
    private RecommendPresenter() {
        mXimalayApi = XimalayApi.getXimalayApi();
    }

    /**
     *description: 根据api 3.10.6 获取猜你喜欢数据
     *usage:
     */
/*    @Override
    public void loadData() {
        //UI为加载状态
        for (IRecommendViewCallback callback: mCallbacks) {
            callback.onLoading();
        }

        Map<String, String> specificParams = new HashMap<>();
        specificParams.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(specificParams, new IDataCallBack<GussLikeAlbumList>() {
            //回调接口已经是主线程了可以更新rv的UI
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                handleSuccess(gussLikeAlbumList);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error ->"+ i +"errorMsg ->"+s);
                handleError();
            }
        });

    }*/

    private void getData(boolean isLoadMore) {
        if (!isLoadMore) {
            currentPage = 1;
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onLoading();
            }
        }else {
            currentPage++;

        }
        mXimalayApi.getHotAlbumList(new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList albumList) {
                handleSuccess(albumList,isLoadMore);
            }

            @Override
            public void onError(int i, String s) {
                handleError();
            }
        },currentPage);
    }

    private void handleSuccess(AlbumList albumList,boolean isLoadMore) {
        List<Album> albums = albumList.getAlbums();
        if (albums != null) {
            if (albums.size() != 0){
                for (IRecommendViewCallback callback: mCallbacks) {
                    callback.onRecommendListLoaded(albums,isLoadMore);
                }
                mCurrentAlbums = albums;
            }else {
                for (IRecommendViewCallback callback: mCallbacks) {
                    callback.onEmpty();
                }
            }
        }
    }

    //更新UI为网络错误页面
    private void handleError() {
        for (IRecommendViewCallback callback: mCallbacks) {
            callback.onNetworkError();
        }
    }
/*    //处理返回数据成功的结果empty or success
    private void handleSuccess(GussLikeAlbumList gussLikeAlbumList) {
        if (gussLikeAlbumList != null) {
            List<Album> albumList = gussLikeAlbumList.getAlbumList();
            if (albumList != null) {
//                albumList.clear();//模拟为空的UI
                if (albumList.size() != 0) {
                    LogUtil.d(TAG,"size -> "+albumList.size());
                    //把请求回来的数据数据传给回调接口,只要是注冊过的都更新数据
                    for (IRecommendViewCallback callback: mCallbacks) {
                        callback.onRecommendListLoaded(albumList);
                    }

                    mCurrentAlbums = albumList;
                }else{
                    for (IRecommendViewCallback callback: mCallbacks) {
                        callback.onEmpty();
                    }
                }
            }
        }
    }*/


    @Override
    public void loadData(Boolean isLoadMore) {
        getData(isLoadMore);
    }

    @Override
    public Album getCurrentAlbum() {
        return mCurrentAlbums.get(0);
    }


    //注册回调接口
    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (callback!=null && !mCallbacks.contains(callback)){
            mCallbacks.add(callback);
        }
    }
    //注销回调接口，避免内存泄漏
    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (callback!=null){
            mCallbacks.remove(callback);
        }
    }
}
