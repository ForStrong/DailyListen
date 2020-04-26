package com.ianf.dailylisten.interfaces;

import com.ianf.dailylisten.base.IBasePresenter;

public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {
    //加载数据
    void loadData();
    //上拉加载更多
    void loadMore();
    //下拉刷新
    void pullRefresh();
}
