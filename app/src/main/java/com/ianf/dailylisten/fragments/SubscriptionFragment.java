package com.ianf.dailylisten.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionFragment extends BaseFragment {

    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_subscription,container,false);
        return rootView;
    }

}
