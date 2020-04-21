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
 *@usage: 通过getFragment(int index)返回fragment
*/
public  class FragmentCreator {

    public final static int INDEX_RECOMMEND = 0;
    public final static int INDEX_SUBSCRIPTION = 1;
    public final static int INDEX_HISTORY = 2;

    public final static int PAGE_COUNT = 3;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<>();

    public static BaseFragment getFragment(int index){
        BaseFragment baseFragment = mFragmentMap.get(index);
        if (baseFragment != null) {
           return baseFragment;
        }
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
        mFragmentMap.put(index,baseFragment);
        return baseFragment;

    }


}
