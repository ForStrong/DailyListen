package com.ianf.dailylisten.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ianf.dailylisten.R;

public class HistoryTrackDialog extends Dialog {
    private View mCanacel;
    private View mConfirm;
    private OnDialogActionClickListener mClickListener = null;
    private CheckBox mCheckBox;

    public HistoryTrackDialog(@NonNull Context context) {
        this(context, 0);
    }

    public HistoryTrackDialog(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected HistoryTrackDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sub_album);
        initView();
        initListener();
    }

    private void initListener() {
        mConfirm.setOnClickListener(v -> {
            if (mClickListener != null) {
                boolean checked = mCheckBox.isChecked();
                mClickListener.onConfirmClick(checked);
                dismiss();
            }
        });

        mCanacel.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onCancelClick();
                dismiss();
            }
        });
    }

    private void initView() {
        mCanacel = this.findViewById(R.id.dialog_check_box_cancel);
        mConfirm = this.findViewById(R.id.dialog_check_box_confirm);
        mCheckBox = this.findViewById(R.id.dialog_check_box);
    }


    public void setOnDialogActionClickListener(OnDialogActionClickListener listener) {
        this.mClickListener = listener;
    }

    public interface OnDialogActionClickListener {
        void onCancelClick();

        void onConfirmClick(boolean isCheck);
    }
}
