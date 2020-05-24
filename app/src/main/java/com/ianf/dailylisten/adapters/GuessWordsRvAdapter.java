package com.ianf.dailylisten.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class GuessWordsRvAdapter extends RecyclerView.Adapter<GuessWordsRvAdapter.GuessWordsHolder> {
    private static final String TAG = "GuessWordsRvAdapter";
    private List<QueryResult> mGuessResults = new ArrayList<>();
    private OnItemClickListener mItemClickListener = null;
    @NonNull
    @Override
    public GuessWordsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guess_words_item_layout
                ,parent,false);
        return new GuessWordsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuessWordsHolder holder, int position) {
        QueryResult queryResult = mGuessResults.get(position);
        String keyword = queryResult.getKeyword();
        holder.mGuessSearchTv.setText(keyword);
        holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClicked(keyword));
    }

    @Override
    public int getItemCount() {
        LogUtil.d(TAG,"mGuessResults size ->"+mGuessResults.size());
        return mGuessResults.size();
    }

    public void setData(List<QueryResult> keyWordList){
        mGuessResults = keyWordList;
    }

    class GuessWordsHolder extends RecyclerView.ViewHolder{

        private final TextView mGuessSearchTv;

        GuessWordsHolder(@NonNull View itemView) {
            super(itemView);
            mGuessSearchTv = itemView.findViewById(R.id.guess_searchItem);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClicked(String keyword);
    }
}
