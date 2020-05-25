package com.ianf.dailylisten.utils;

import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.fragments.SignInFragment;
import com.ianf.dailylisten.fragments.SignUpFragment;

import java.util.HashMap;

public class LoginFragmentCreator {
    private final static int SING_IN_FRAGMENT = 0;
    private final static int SING_UP_FRAGMENT = 1;
    public final static int SIZE_FRAGMENT = 2;

    private static  HashMap<Integer,BaseFragment> sFragmentHashMap = new HashMap<>();

    public static BaseFragment getFragment(int index){
        BaseFragment baseFragment = sFragmentHashMap.get(index);
        //不为空直接获取
        if (baseFragment != null) {
            return baseFragment;
        }
        //为空创建对应的fragment并加入map集合
        switch (index){
            case SING_IN_FRAGMENT:
                baseFragment = new SignInFragment();
                break;
            case SING_UP_FRAGMENT:
                baseFragment = new SignUpFragment();
                break;
        }
        sFragmentHashMap.put(index,baseFragment);
        return baseFragment;

    }
}
