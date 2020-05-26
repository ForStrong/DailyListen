package com.ianf.dailylisten.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class MyAlbum extends BmobObject {
    private String coverUrl;
    private String title;
    private String description;
    private long tracksCount;
    private String authorName;
    private long playCount;
    private long albumId;
    private BmobUser user;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(long tracksCount) {
        this.tracksCount = tracksCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
