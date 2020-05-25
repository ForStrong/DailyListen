package com.ianf.dailylisten.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ianf.dailylisten.utils.LoginFragmentCreator;

public class LoginViewPagerAdapter extends FragmentPagerAdapter {

    public LoginViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return LoginFragmentCreator.getFragment(position);
    }

    @Override
    public int getCount() {
        return LoginFragmentCreator.SIZE_FRAGMENT;
    }
}
