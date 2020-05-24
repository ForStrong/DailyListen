package com.ianf.dailylisten.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ianf.dailylisten.Presenters.DetailPresenter;
import com.ianf.dailylisten.Presenters.SearchPresenter;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.AlbumRvAdapter;
import com.ianf.dailylisten.adapters.GuessWordsRvAdapter;
import com.ianf.dailylisten.interfaces.ISearchViewCallback;
import com.ianf.dailylisten.utils.LogUtil;
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
    private static final String TAG = "SearchActivity";
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
    private GuessWordsRvAdapter mGuessWordsRvAdapter;
    private AlbumRvAdapter mAlbumRvAdapter;
    private List<Album> mAlbumList = new ArrayList<>();
    private boolean mIsUser = true;

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
        mDeleteIv.setOnClickListener(v -> {
            mKeywordEt.setText("");
            mKeywordEt.requestFocus();
            mInputMethodManager.showSoftInput(mKeywordEt, InputMethodManager.SHOW_IMPLICIT);
        });
        mBackIv.setOnClickListener(v -> finish());
        mSearchTv.setOnClickListener(v -> {
            String keyWord = mKeywordEt.getText().toString().trim();
            if (TextUtils.isEmpty(keyWord)) {
                //可以给个提示
                Toast.makeText(SearchActivity.this, "搜索关键字不能为空.", Toast.LENGTH_SHORT).show();
            } else {
                mSearchPresenter.getDataByKeyword(keyWord);
                mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
            }
        });
        //用户输入字体改变时，去获取联想词
        mKeywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mIsUser) {
                    String word = s.toString();
                    if (word.length() > 0) {
                        if (mSearchPresenter != null) {
                            mSearchPresenter.getGuessWords(word);
                        }
                    } else {
                        mSearchPresenter.getHotWords();
                    }
                }
                mIsUser = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mHotWordsLayout.setClickListener(this::doSearch);

        mDataLoadedRefreshLayout.setOnLoadMoreListener(refreshLayout -> mSearchPresenter.loadMore());
    }

    private void initPresenter() {
        mSearchPresenter = SearchPresenter.getInstance();
        mSearchPresenter.registerViewCallback(this);
    }

    private void initView() {
        mInputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        mBackIv = findViewById(R.id.search_backIv);
        mDeleteIv = findViewById(R.id.search_deleteIv);
        mKeywordEt = findViewById(R.id.search_keywordEt);
        //进去0.5秒弹出键盘
        mKeywordEt.postDelayed(() -> {
            mKeywordEt.requestFocus();
            mInputMethodManager.showSoftInput(mKeywordEt, InputMethodManager.SHOW_IMPLICIT);
        }, TIME_SHOW_IMM);
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
        //成功的画面，热词，联想词，结果
        View successView = LayoutInflater.from(this).inflate(R.layout.search_success_layout, null);
        //热词
        mHotWordsLayout = successView.findViewById(R.id.search_hotWords_layout);
        //搜索结果
        mDataLoadedRefreshLayout = successView.findViewById(R.id.search_dataLoadedRefreshLayout);
        mDataLoadedRv = successView.findViewById(R.id.search_dataLoadedRv);
        LinearLayoutManager linearDataLoadedLayoutManager = new LinearLayoutManager(this);
        mAlbumRvAdapter = new AlbumRvAdapter();
        mAlbumRvAdapter.setAlbumItemClickListener((tag, album) -> {
            DetailPresenter.getInstance().setAlbumByRecommend(album);
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            startActivity(intent);
        });
        mDataLoadedRv.setLayoutManager(linearDataLoadedLayoutManager);
        mDataLoadedRv.setAdapter(mAlbumRvAdapter);
        mDataLoadedRefreshLayout.setEnableRefresh(false);
        mDataLoadedRefreshLayout.setEnableOverScrollBounce(true);
        mDataLoadedRefreshLayout.setEnableOverScrollDrag(true);
        //联想词
        mGuessWordsRv = successView.findViewById(R.id.search_guessWordsRv);
        LinearLayoutManager linearGuessWordsLayoutManager = new LinearLayoutManager(this);
        mGuessWordsRv.setLayoutManager(linearGuessWordsLayoutManager);
        mGuessWordsRvAdapter = new GuessWordsRvAdapter();
        mGuessWordsRvAdapter.setOnItemClickListener(keyword -> doSearch(keyword));
        mGuessWordsRv.setAdapter(mGuessWordsRvAdapter);
        return successView;
    }

    //=======================================viewCallback start=====================================
    @Override
    public void onHotWordsLoaded(List<HotWord> hotWordList) {
        hideSuccessView();
        mHotWordsLayout.setVisibility(View.VISIBLE);
        LogUtil.d(TAG, "hotWordList size ->" + hotWordList.size());
        //改变UILoader状态
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
    public void onDataLoaded(List<Album> albums, boolean isLoadMore) {
        //隐藏键盘
        mInputMethodManager.hideSoftInputFromWindow(mKeywordEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        hideSuccessView();
        mDataLoadedRefreshLayout.setVisibility(View.VISIBLE);
        if (mUiLoader != null && albums.size() > 0) {
            if (isLoadMore) {
                mAlbumList.addAll(albums);
            } else {
                mAlbumList = albums;
            }
            mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
            mAlbumRvAdapter.setData(mAlbumList);
            mAlbumRvAdapter.notifyDataSetChanged();
        } else if (!isLoadMore && mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.EMPTY);
        } else if (isLoadMore && albums.size() == 0) {
            Toast.makeText(this, "没有更多精彩内容", Toast.LENGTH_SHORT).show();
        }
        if (isLoadMore) {
            mDataLoadedRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onGuessWordsLoaded(List<QueryResult> keyWordList) {
        hideSuccessView();
        LogUtil.d(TAG, "keyWordList size ->" + keyWordList.size());
        mGuessWordsRv.setVisibility(View.VISIBLE);
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.SUCCESS);
        }
        mGuessWordsRvAdapter.setData(keyWordList);
        mGuessWordsRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.NETWORK_ERROR);
        }
    }

    private void hideSuccessView() {
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


    private void doSearch(String keyword) {
        if (mUiLoader != null) {
            mUiLoader.upDataUIStatus(UILoader.UIStatus.LOADING);
        }
        if (mSearchPresenter != null) {
            mSearchPresenter.getDataByKeyword(keyword);
            mIsUser = false;
            mKeywordEt.setText(keyword);
            mKeywordEt.setSelection(keyword.length());
        }
    }
}
