package com.ianf.dailylisten.Presenters;

import com.ianf.dailylisten.interfaces.IDetailPresenter;
import com.ianf.dailylisten.interfaces.IDetailViewCallback;
import com.ianf.dailylisten.utils.Constants;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPresenter implements IDetailPresenter {
    private static final String TAG = "DetailPresenter";
    private Album mAlbumByRecommend;
    private List<IDetailViewCallback> mCallbacks = new ArrayList<>();
    //单例设计模式
    private static DetailPresenter sDetailPresenter = null;
    public static DetailPresenter getInstance() {
        if (sDetailPresenter == null) {
            synchronized (DetailPresenter.class){
                if (sDetailPresenter == null) {
                    sDetailPresenter = new DetailPresenter();
                }
            }
        }
        return sDetailPresenter;
    }
    private DetailPresenter(){

    }

    public void setAlbumByRecommend(Album album) {
        mAlbumByRecommend = album;
    }
    //注册接口的时候，detailActivity也能获取到从Recommend来的album
    public void registerViewCallback(IDetailViewCallback callback){
        if (callback != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
            if (mAlbumByRecommend != null) {
                callback.getAlbumByRecommend(mAlbumByRecommend);
            }
        }
    }
    //注销注册接口，以免内存泄漏
    public void unRegisterViewCallback(IDetailViewCallback callback){
        if (callback != null ) {
            mCallbacks.remove(callback);
        }
    }

    /**
    *description:通过api获取数据 api:3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表
    *usage: page >= 1,album_id由recommendF传过来的album获得
    */
    @Override
    public void loadData(int album_id,int page) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, album_id+"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page+"");
        //默认50条
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_TRACKS_PAGE_SIZE + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG,"tracks size ->"+tracks.size());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"errorCode -> " + i);
                LogUtil.d(TAG,"errorMsg -> " + s);


            }
        });
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void pullRefresh() {

    }
}
