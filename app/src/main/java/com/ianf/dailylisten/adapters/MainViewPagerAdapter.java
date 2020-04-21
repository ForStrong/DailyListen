package com.ianf.dailylisten.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ianf.dailylisten.utils.FragmentCreator;
/**
*create by IANDF in 2020/4/21
 *lastTime:
 *@description: MainActivity中ViewPager的适配器
 *@usage: viewpager通过adapter调节fragment和fragment count
*/
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //fragment的位置和indicator是一致的，通过绑定完成
        return FragmentCreator.getFragment(position);
    }

    @Override
    public int getCount() {
        return FragmentCreator.PAGE_COUNT;
    }
}
