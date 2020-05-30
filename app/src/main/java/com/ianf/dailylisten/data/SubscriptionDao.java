package com.ianf.dailylisten.data;

import android.util.Log;

import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

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
    private List<Album> mAlbumList = new ArrayList<>();
    private ISubDaoCallback mISubDaoCallback = null;

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
        mISubDaoCallback = callback;
    }

    @Override
    public void addAlbum(Album album) {
        MyAlbum myAlbum = albumExchangeMyAlbum(album);
        myAlbum.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtil.d(TAG, Thread.currentThread().getName());
                LogUtil.d(TAG, "s" + s);
                if (e == null) {
                    LogUtil.d(TAG, "no exception");
                    mISubDaoCallback.onAddResult(true);
                } else {
                    LogUtil.d(TAG, "exception" + e.getMessage());
                    mISubDaoCallback.onAddResult(false);

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
        album.setId(myAlbum.getAlbumId());

        Announcer announcer = new Announcer();
        announcer.setNickname(myAlbum.getAuthorName());
        album.setAnnouncer(announcer);
        return album;
    }

    @Override
    public void delAlbum(Album album) {
        MyAlbum myAlbum = albumExchangeMyAlbum(album);
        BmobQuery<MyAlbum> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.addWhereEqualTo("albumId", album.getId());
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyAlbum>() {
            @Override
            public void done(List<MyAlbum> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        myAlbum.delete(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mISubDaoCallback.onDelResult(true);
                                } else {
                                    mISubDaoCallback.onDelResult(false);
                                }
                            }
                        });
                    }
                }else {
                    mISubDaoCallback.onDelResult(false);
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
            public void done(List<MyAlbum> myAlbums, BmobException e) {
                if (e == null) {
                    LogUtil.d(TAG, "查询成功");
                    LogUtil.d(TAG, "myAlbums size -->" + myAlbums.size());
                    mAlbumList.clear();
                    for (MyAlbum myAlbum : myAlbums) {
                        Album album = myAlbumExchangeAlbum(myAlbum);
                        LogUtil.d(TAG, "mISubDaoCallback -->" + mISubDaoCallback.getClass());
                        mAlbumList.add(album);
                    }
                    //通知presenter albums 加载完成
                    mISubDaoCallback.onSubListLoaded(mAlbumList);
                    LogUtil.d(TAG, "mAlbumList size -->" + mAlbumList.size());
                } else {
                    //加载数据失败
                    mISubDaoCallback.onSubListError();
                    Log.e("BMOB", e.toString());
                }
            }

        });

    }

    @Override
    public void isSub(Album album) {
        //判断该用户是否已经订阅，返回结果给presenter
        BmobQuery<MyAlbum> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.addWhereEqualTo("albumId", album.getId());
        query.findObjects(new FindListener<MyAlbum>() {
            @Override
            public void done(List<MyAlbum> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mISubDaoCallback.isASub(true);
                    } else {
                        mISubDaoCallback.isASub(false);
                    }
                }
            }
        });
    }
}
