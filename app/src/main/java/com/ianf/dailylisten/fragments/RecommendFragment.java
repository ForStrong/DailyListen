package com.ianf.dailylisten.fragments;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseFragment {


    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_recommend,container,false);
        return rootView;
    }

}
