package com.ianf.dailylisten.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ianf.dailylisten.Presenters.SearchPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.interfaces.ISearchViewCallback;
import com.ianf.dailylisten.views.FlowTextLayout;
import com.ianf.dailylisten.views.UILoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ISearchViewCallback {

    private TextView mSearchTv;
    private EditText mKeywordEt;
    private ImageView mDeleteIv;
    private ImageView mBackIv;
    private SearchPresenter mSearchPresenter;
    private FrameLayout mSearchContainer;
    private static final int TIME_SHOW_IMM = 500;
    private InputMethodManager mInputMethodManager;
    private FlowTextLayout mHotWordsLayout;
    private SmartRefreshLayout mDataLoadedRefreshLayout;
    private RecyclerView mDataLoadedRv;
    private RecyclerView mGuessWordsRv;
    private UILoader mUiLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initPresenter();
        initEvent();
        mSearchPresenter.getHotWords();
    }

    private void initEvent() {

    }

    private void initPresenter() {
        mSearchPresenter = SearchPresenter.getInstance();
        mSearchPresenter.registerViewCallback(this);
    }

    private void initView() {
        mInputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        mBackIv = findViewById(R.id.search_backIv);
        mDeleteIv = findViewById(R.id.search_deleteIv);
        mKeywordEt = findViewById(R.id.search_keywordEt);
        //进去0.5秒弹出键盘
        mKeywordEt.postDelayed(() -> {
            mKeywordEt.requestFocus();
            mInputMethodManager.showSoftInput(mKeywordEt,InputMethodManager.SHOW_IMPLICIT);
        },TIME_SHOW_IMM);
        mSearchTv = findViewById(R.id.search_tv);
        mSearchContainer = findViewById(R.id.search_container_layout);
        mUiLoader = new UILoader(this) {
            @Override
            public View getSuccessView(ViewGroup container) {
                return initSuccessView();
            }
        };
        if (mUiLoader.getParent() instanceof ViewGroup) {
           ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        mSearchContainer.addView(mUiLoader);
    }

    private View initSuccessView() {
        //TODO:成功的画面，热词，联想词，结果
        View successView = LayoutInflater.from(this).inflate(R.layout.search_success_layout,null);
        //热词
        mHotWordsLayout = successView.findViewById(R.id.search_hotWords_layout);
        //搜索结果
        mDataLoadedRefreshLayout = successView.findViewById(R.id.search_dataLoadedRefreshLayout);
        mDataLoadedRv = successView.findViewById(R.id.search_dataLoadedRv);
        //联想词
        mGuessWordsRv = successView.findViewById(R.id.search_guessWordsRv);
        return successView;
    }

    //=======================================viewCallback start=====================================
    @Override
    public void onHotWordsLoaded(List<HotWord> hotWordList) {
        hideSuccessView();
        mHotWordsLayout.setVisibility(View.VISIBLE);
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
        }
        List<String> hotWords = new ArrayList<>();
        hotWords.clear();
        for (HotWord hotWord : hotWordList) {
            String searchWord = hotWord.getSearchword();
            hotWords.add(searchWord);
        }
        Collections.sort(hotWords);
        //给数据给控件
        mHotWordsLayout.setTextContents(hotWords);
    }

    @Override
    public void onDataLoaded(List<Album> result) {

    }

    @Override
    public void onGuessWordsLoaded(List<QueryResult> keyWordList) {

    }

    @Override
    public void onError() {

    }

    private void hideSuccessView(){
        mDataLoadedRefreshLayout.setVisibility(View.GONE);
        mGuessWordsRv.setVisibility(View.GONE);
        mHotWordsLayout.setVisibility(View.GONE);
    }
    //=======================================viewCallback end=======================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            mSearchPresenter.unRegisterViewCallback(this);
        }
    }
}
