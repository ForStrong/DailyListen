package com.ianf.dailylisten.base;

public interface IBasePresenter<T> {

    /**
     * 注册UI的回调接口
     *
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册
     *
     * @param t
     */
    void unRegisterViewCallback(T t);
}
