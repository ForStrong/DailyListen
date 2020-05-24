package com.ianf.dailylisten.interfaces;

import com.ianf.dailylisten.base.IBasePresenter;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {
    /**
    *description:获取热词
    */
    void getHotWords();
    /**
    *description:根据关键字获取数据
    */
    void getDataByKeyword(String keyword);
    /**
    *description:根据输入单词生成联想词
    */
    void getGuessWords(String guessWord);
    /**
    *description:加载更多
    */
    void loadMore();

}
