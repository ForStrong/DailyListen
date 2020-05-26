package com.ianf.dailylisten.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ianf.dailylisten.MainActivity;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.activities.LoginActivity;
import com.ianf.dailylisten.base.BaseFragment;

import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends BaseFragment {
    private static final String TAG = "SignInFragment";
    private TextInputEditText mNameTv;
    private TextInputEditText mPasswordTv;
    private Button mSingInBt;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
    }

    private void initEvent() {
        mSingInBt.setOnClickListener(v -> {
            BmobUser user = new BmobUser();
            String userName = Objects.requireNonNull(mNameTv.getText()).toString();
            String password = Objects.requireNonNull(mPasswordTv.getText()).toString();
            user.setUsername(userName);
            user.setPassword(password);
            boolean isEmpty = isEmpty(userName, password);
            if (!isEmpty){
                user.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            LoginActivity loginActivity = (LoginActivity)getActivity();
                            startActivity(new Intent(loginActivity,MainActivity.class));
                            assert loginActivity != null;
                            loginActivity.finish();
                        } else {
                            Log.d(TAG, "errorMsg: "+e.getMessage());
                            Log.d(TAG, "errorCode: "+e.getErrorCode());
                            Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }else {
                Toast.makeText(getContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmpty(String... strings) {
        for (String string : strings) {
            if (TextUtils.isEmpty(string)){
                return true;
            }
        }
        return false;
    }
    private void initView(View view) {
        mNameTv = view.findViewById(R.id.sign_in_user_nameTv);
        mPasswordTv = view.findViewById(R.id.sign_in_passwordTv);
        mSingInBt = view.findViewById(R.id.sign_in_loginBt);
    }
}
