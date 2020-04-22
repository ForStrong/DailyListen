package com.ianf.dailylisten.interfaces;

public interface IRecommendPresenter {
    //加载数据
    void loadData();
    //上拉加载更多
    void loadMore();
    //下拉刷新
    void pullRefresh();
}
