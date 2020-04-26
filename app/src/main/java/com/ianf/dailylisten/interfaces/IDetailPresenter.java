package com.ianf.dailylisten.interfaces;

import com.ianf.dailylisten.base.IBasePresenter;

public interface IDetailPresenter extends IBasePresenter<IDetailViewCallback> {
    //加载数据
    void loadData(int album_id,int page);
    //上拉加载更多
    void loadMore();
    //下拉刷新
    void pullRefresh();
}
