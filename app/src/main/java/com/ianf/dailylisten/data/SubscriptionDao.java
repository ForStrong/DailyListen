package com.ianf.dailylisten.data;

import android.util.Log;

import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SubscriptionDao implements ISubDao {
    private static final String TAG = "SubscriptionDao";
    private List<ISubDaoCallback> mISubDaoCallbacks = new ArrayList<>();
    private List<Album> mAlbumList = new ArrayList<>();

    private SubscriptionDao() {
    }

    private static class InnerHolder {
        private static final SubscriptionDao INSTANCE = new SubscriptionDao();
    }

    public static SubscriptionDao getInstance() {
        return InnerHolder.INSTANCE;
    }

    @Override
    public void setCallback(ISubDaoCallback callback) {
        if (callback != null && !mISubDaoCallbacks.contains(callback)) {
            mISubDaoCallbacks.add(callback);
        }
    }

    @Override
    public void addAlbum(Album album) {
        MyAlbum myAlbum = albumExchangeMyAlbum(album);
        myAlbum.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtil.d(TAG,Thread.currentThread().getName());
                LogUtil.d(TAG,"s"+s);
                if (e == null) {
                    LogUtil.d(TAG,"no exception");
                    for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                        iSubDaoCallback.onAddResult(true);
                    }
                } else {
                    LogUtil.d(TAG,"exception" + e.getMessage());
                    for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                        iSubDaoCallback.onAddResult(false);
                    }
                }
            }
        });
    }

    //把album转成Album
    private MyAlbum albumExchangeMyAlbum(Album album) {
        MyAlbum myAlbum = new MyAlbum();
        myAlbum.setCoverUrl(album.getCoverUrlLarge());
        myAlbum.setTitle(album.getAlbumTitle());
        myAlbum.setDescription(album.getAlbumIntro());
        myAlbum.setTracksCount(album.getIncludeTrackCount());
        myAlbum.setPlayCount(album.getPlayCount());
        myAlbum.setAuthorName(album.getAnnouncer().getNickname());
        myAlbum.setAlbumId(album.getId());
        myAlbum.setUser(BmobUser.getCurrentUser(BmobUser.class));
        return myAlbum;
    }

    //把MyAlbum转成Album
    private Album myAlbumExchangeAlbum(MyAlbum myAlbum) {
        Album album = new Album();
        album.setCoverUrlLarge(myAlbum.getCoverUrl());
        album.setAlbumTitle(myAlbum.getTitle());
        album.setAlbumIntro(myAlbum.getDescription());
        album.setIncludeTrackCount(myAlbum.getTracksCount());
        album.setPlayCount(myAlbum.getPlayCount());
        album.getAnnouncer().setNickname(myAlbum.getAuthorName());
        album.setId(myAlbum.getAlbumId());
        return album;
    }

    @Override
    public void delAlbum(Album album) {
        MyAlbum myAlbum = albumExchangeMyAlbum(album);
        final String[] aa = new String[1];
        BmobQuery<MyAlbum> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.addWhereEqualTo("albumId", album.getId());
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyAlbum>() {
            @Override
            public void done(List<MyAlbum> list, BmobException e) {
                if (list.size() > 0) {
                    myAlbum.delete(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                                    iSubDaoCallback.onDelResult(true);
                                }
                            } else {
                                for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                                    iSubDaoCallback.onDelResult(false);
                                }
                            }
                        }
                    });
                }
            }
        });

    }


    @Override
    public void getAlbums() {
        BmobQuery<MyAlbum> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyAlbum>() {
            @Override
            public void done(List<MyAlbum> albums, BmobException e) {

                if (e == null) {
                    LogUtil.d(TAG, "查询成功");
                    LogUtil.d(TAG, "myAlbums size -->" + albums.size());
                    for (MyAlbum myAlbum : albums) {
                        Album album = myAlbumExchangeAlbum(myAlbum);
                        mAlbumList.add(album);
                    }
                    //通知presenter albums 加载完成
                    for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                        iSubDaoCallback.onSubListLoaded(mAlbumList);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }

        });

    }

    @Override
    public void isSub(Album album) {
        //TODO:判断该用户是否已经订阅，返回结果给presenter
        BmobQuery<MyAlbum> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.addWhereEqualTo("albumId", album.getId());
        query.findObjects(new FindListener<MyAlbum>() {
            @Override
            public void done(List<MyAlbum> list, BmobException e) {
                if (list.size() > 0){
                    for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                        iSubDaoCallback.isASub(true);
                    }
                }else {
                    for (ISubDaoCallback iSubDaoCallback : mISubDaoCallbacks) {
                        iSubDaoCallback.isASub(false);
                    }
                }
            }
        });
    }
}
