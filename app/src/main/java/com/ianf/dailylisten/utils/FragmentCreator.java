package com.ianf.dailylisten.utils;

import android.annotation.SuppressLint;

import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.fragments.HistoryFragment;
import com.ianf.dailylisten.fragments.RecommendFragment;
import com.ianf.dailylisten.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;
/**
*create by IANDF in 2020/4/21
 *lastTime:
 *@description: 用来创建和存储fragment
 *@usage: 通过getFragment(int index)返回fragment,用于viewPager切换fragment
*/
public  class FragmentCreator {
    private static final String TAG = "FragmentCreator";
    public final static int INDEX_RECOMMEND = 0;
    public final static int INDEX_SUBSCRIPTION = 1;
    public final static int INDEX_HISTORY = 2;
    //fragment数量
    public final static int PAGE_COUNT = 3;
    //hashMap保证元素的唯一性，便于对fragment进行缓存和切换
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<>();
    /**
    *description: 根据索引获取对应的Fragment
    *usage:
    */
    public static BaseFragment getFragment(int index){
        BaseFragment baseFragment = mFragmentMap.get(index);
        //不为空直接获取
        if (baseFragment != null) {
           return baseFragment;
        }
        //为空创建对应的fragment并加入map集合
        switch (index){
            case INDEX_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;
        }
        LogUtil.d(TAG,baseFragment.toString() + "");
        mFragmentMap.put(index,baseFragment);
        return baseFragment;

    }


}
