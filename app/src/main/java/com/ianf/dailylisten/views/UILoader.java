package com.ianf.dailylisten.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.utils.LogUtil;

/**
*create by IANDF in 2020/4/23
 *lastTime:
 *@description: 通过网络请求数据的状态加载不同的UI
 *@usage: 加载数据的时候是加载页面的UI，在presenter里面使用回调接口，通过接口中的方法更新UI
 *god bless my code
*/
public abstract class UILoader extends FrameLayout {
    private static final String TAG = "UILoader";
    private View mNetworkErrorView;
    private View mLoadingView;
    private View mSuccessView;
    private View mEmptyView;
    //使用枚举列出各种状态
    public enum UIStatus{
        NETWORK_ERROR,LOADING,SUCCESS,EMPTY,NONE;
    }
    //初始化当前状态
    private UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        switchUIByStatus();
    }
    //使用mCurrentStatus更新UI
    private void switchUIByStatus() {
        if (mNetworkErrorView == null){
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        mNetworkErrorView.setVisibility((mCurrentStatus == UIStatus.NETWORK_ERROR) ? VISIBLE : GONE);

        if (mLoadingView == null){
            mLoadingView = getLoadingViewView();
            addView(mLoadingView);
        }
        mLoadingView.setVisibility((mCurrentStatus == UIStatus.LOADING) ? VISIBLE : GONE);

        if (mSuccessView == null){
            mSuccessView = getSuccessViewView();
            addView(mSuccessView);
        }
        mSuccessView.setVisibility((mCurrentStatus == UIStatus.SUCCESS) ? VISIBLE : GONE);

        if (mEmptyView == null){
            mEmptyView = getEmptyViewView();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility((mCurrentStatus == UIStatus.EMPTY) ? VISIBLE : GONE);


    }

    private View getEmptyViewView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    public abstract View getSuccessViewView();

    private View getLoadingViewView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    private View getNetworkErrorView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_network_error_view,this,false);

    }

    public void upDataUIStatus(UIStatus status){
        mCurrentStatus = status;
        LogUtil.d(TAG,"thread -> "+Thread.currentThread().getName());
        //更新UI一定要在主线程完成，所以在BaseApplication中创建Handler，通过 Handler通知主线程，从而在主线程更新U操作
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByStatus();
            }
        });
    }


}
