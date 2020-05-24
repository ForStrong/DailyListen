package com.ianf.dailylisten.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ianf.dailylisten.Presenters.SearchPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.interfaces.ISearchViewCallback;
import com.ianf.dailylisten.views.FlowTextLayout;
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
        mKeywordEt.postDelayed(() -> {
            mKeywordEt.requestFocus();
            mInputMethodManager.showSoftInput(mKeywordEt,InputMethodManager.SHOW_IMPLICIT);
        },TIME_SHOW_IMM);
        mSearchTv = findViewById(R.id.search_tv);
        mSearchContainer = findViewById(R.id.search_container_layout);
    }

    @Override
    public void onHotWordsLoaded(List<HotWord> hotWordList) {
        FlowTextLayout flowTextLayout = new FlowTextLayout(this);
        List<String> hotWords = new ArrayList<>();
        hotWords.clear();
        for (HotWord hotWord : hotWordList) {
            String searchWord = hotWord.getSearchword();
            hotWords.add(searchWord);
        }
        Collections.sort(hotWords);
        flowTextLayout.setTextContents(hotWords);
        mSearchContainer.addView(flowTextLayout);
    }

    @Override
    public void onDataLoaded(List<Album> result) {

    }

    @Override
    public void onGuessWordsLoaded(List<QueryResult> keyWordList) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            mSearchPresenter.unRegisterViewCallback(this);
        }
    }
}
