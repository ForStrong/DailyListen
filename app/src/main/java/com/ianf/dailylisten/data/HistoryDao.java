package com.ianf.dailylisten.data;

import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HistoryDao implements IHistoryDao {
    private static final String TAG = "HistoryDao";
    private IHistoryDaoViewCallback mViewCallback;

    private HistoryDao() {
    }

    private static class InnerHolder{
        static final HistoryDao HISTORY_DAO = new HistoryDao();
    }

    public static HistoryDao getInstance(){
        return InnerHolder.HISTORY_DAO;
    }

    @Override
    public void setCallback(IHistoryDaoViewCallback callback) {
        mViewCallback = callback;
    }

    @Override
    public void addHistory(Track track) {
        MyTrack myTrack = trackExchangeMyTrack(track);
        myTrack.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtil.d(TAG, Thread.currentThread().getName());
                LogUtil.d(TAG, "s" + s);
                if (e == null) {
                    LogUtil.d(TAG, "no exception");
                    mViewCallback.onHistoryAdd(true);
                } else {
                    LogUtil.d(TAG, "exception" + e.getMessage());
                    mViewCallback.onHistoryAdd(false);
                }
            }
        });
    }

    private MyTrack trackExchangeMyTrack(Track track) {
        MyTrack myTrack = new MyTrack();
        myTrack.setCoverUrlLarge(track.getCoverUrlLarge());
        myTrack.setDataId(track.getDataId());
        myTrack.setDuration(track.getDuration());
        myTrack.setNickname(track.getAnnouncer().getNickname());
        myTrack.setPlayCount(track.getPlayCount());
        myTrack.setTrackTitle(track.getTrackTitle());
        myTrack.setUpdatedDate(track.getUpdatedAt());
        myTrack.setUser(BmobUser.getCurrentUser(BmobUser.class));
        return myTrack;
    }

    private Track myTrackExchangeTrack(MyTrack myTrack) {
        Track track = new Track();
        track.setDataId(myTrack.getDataId());
        track.setDuration(myTrack.getDuration());
        track.setPlayCount(myTrack.getPlayCount());
        track.setTrackTitle(myTrack.getTrackTitle());
        track.setUpdatedAt(myTrack.getUpdatedDate());
        track.setCoverUrlLarge(myTrack.getCoverUrlLarge());
        track.setCoverUrlMiddle(myTrack.getCoverUrlLarge());
        track.setCoverUrlSmall(myTrack.getCoverUrlLarge());

        Announcer announcer = new Announcer();
        announcer.setNickname(myTrack.getNickname());
        track.setAnnouncer(announcer);
        return track;
    }

    @Override
    public void delHistory(Track track) {
        MyTrack myTrack = trackExchangeMyTrack(track);
        BmobQuery<MyTrack> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.addWhereEqualTo("dataId", myTrack.getDataId());
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyTrack>() {
            @Override
            public void done(List<MyTrack> list, BmobException e) {
                if (list.size() > 0) {
                    myTrack.delete(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mViewCallback.onHistoryDel(true);
                            } else {
                                mViewCallback.onHistoryDel(false);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void clearHistory() {
        BmobQuery<MyTrack> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyTrack>() {
            @Override
            public void done(List<MyTrack> myTracks, BmobException e) {
                for (MyTrack myTrack : myTracks) {
                    myTrack.delete(myTrack.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                }
                mViewCallback.onHistoriesClean(true);
            }
        });
    }

    @Override
    public void listHistories() {
        BmobQuery<MyTrack> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        query.order("-updatedAt");
        query.findObjects(new FindListener<MyTrack>() {
            @Override
            public void done(List<MyTrack> myTracks, BmobException e) {
                List<Track> trackList = new ArrayList<>();
                trackList.clear();
                LogUtil.d(TAG,"myTracks size ->" + myTracks.size());
                for (MyTrack myTrack : myTracks) {
                    Track track = myTrackExchangeTrack(myTrack);
                    trackList.add(track);
                }
                mViewCallback.onHistoriesLoaded(trackList);
            }
        });
    }
}
