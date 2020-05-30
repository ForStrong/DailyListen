package com.ianf.dailylisten.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ianf.dailylisten.Presenters.HistoryPresenter;
import com.ianf.dailylisten.Presenters.PlayerPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.activities.PlayerActivity;
import com.ianf.dailylisten.adapters.DetailRvAdapter;
import com.ianf.dailylisten.base.BaseApplication;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.interfaces.IHistoryPresenterViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
import com.ianf.dailylisten.views.HistoryTrackDialog;
import com.ianf.dailylisten.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

import cn.bmob.v3.http.I;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment implements IHistoryPresenterViewCallback, HistoryTrackDialog.OnDialogActionClickListener {

    private DetailRvAdapter mDetailRvAdapter;
    private UILoader mUiLoader;
    private HistoryPresenter mHistoryPresenter;
    private static final String TAG = "HistoryFragment";
    private HistoryTrackDialog mDialog;
    private Track mCurrentTrack;

    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        FrameLayout historyContainer = rootView.findViewById(R.id.history_container);
        mUiLoader = new UILoader(container.getContext()) {
            @Override
            public View getSuccessView(ViewGroup container) {
                return initSuccessView(inflater, container);
            }
        };
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        historyContainer.addView(mUiLoader);
        initPresenter();
        return rootView;
    }

    private void initPresenter() {
        mHistoryPresenter = HistoryPresenter.getInstance();
        mHistoryPresenter.registerViewCallback(this);
        mHistoryPresenter.listHistories();
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
            mUiLoader.setOnRetryListener(() -> {
                mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
                mHistoryPresenter.listHistories();
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHistoryPresenter.unRegisterViewCallback(this);
    }

    private View initSuccessView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.history_success_layout, container, false);
        RecyclerView historyRv = view.findViewById(R.id.history_success_historyRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        historyRv.setLayoutManager(linearLayoutManager);
        mDetailRvAdapter = new DetailRvAdapter();
        historyRv.setAdapter(mDetailRvAdapter);
        mDetailRvAdapter.setItemClickListener((tracks, position) -> {
            PlayerPresenter.getInstance().setTrackList(tracks, position);
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            startActivity(intent);
        });
        mDetailRvAdapter.setOnLongClickListener((tracks, position) -> {
            mCurrentTrack = tracks.get(position);
            //弹窗删除按钮
            mDialog = new HistoryTrackDialog(getContext());
            mDialog.setOnDialogActionClickListener(this);
            mDialog.show();
        });
        return view;
    }

    //=============IHistoryPresenterViewCallback start==================================================
    @Override
    public void onHistoriesLoaded(List<Track> tracks) {
        if (tracks.size() > 0) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
        } else {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.EMPTY);
        }
        LogUtil.d(TAG, "tracks size ->" + tracks.size());
        mDetailRvAdapter.setData(tracks);
        mDetailRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHistoriesLoadedError() {
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.NETWORK_ERROR);
        }
    }

    @Override
    public void onHistoryAddResult(boolean isSuccess) {
        if (isSuccess)
            mHistoryPresenter.listHistories();
    }

    @Override
    public void onHistoryDeleteResult(boolean isSuccess) {
        if (isSuccess)
            mHistoryPresenter.listHistories();
        else
            Toast.makeText(getActivity(), "清除历史记录失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCleanedHistory(boolean isSuccess) {
        if (isSuccess)
            mHistoryPresenter.listHistories();
        else
            Toast.makeText(getActivity(), "清除历史记录失败!", Toast.LENGTH_SHORT).show();
    }

//=============IHistoryPresenterViewCallback end====================================================

    //=============OnDialogActionClickListener start====================================================
    @Override
    public void onCancelClick() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onConfirmClick(boolean isCheck) {
        if (isCheck) {
            mHistoryPresenter.cleanHistories();
        } else {
            mHistoryPresenter.delHistory(mCurrentTrack);
        }

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
//=============OnDialogActionClickListener end======================================================
}
