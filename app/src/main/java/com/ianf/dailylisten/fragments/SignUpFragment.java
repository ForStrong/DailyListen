package com.ianf.dailylisten.fragments;

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
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.ianf.dailylisten.R;
import com.ianf.dailylisten.activities.LoginActivity;
import com.ianf.dailylisten.base.BaseFragment;
import com.ianf.dailylisten.utils.LoginFragmentCreator;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment {
    private static final String TAG = "SignUpFragment";
    private Button mSignUpBt;
    private TextInputEditText mPasswordTv;
    private TextInputEditText mNameTv;

    public SignUpFragment() {

    }

    @Override
    protected View onSubViewLoad(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
    }

    private void initEvent() {
        mSignUpBt.setOnClickListener(v -> {
            signUp();
        });
    }

    private void signUp() {
        String password = mPasswordTv.getText().toString();
        String userName = mNameTv.getText().toString();
        boolean isEmpty = isEmpty(userName, password);
        if (!isEmpty){
            BmobUser user = new BmobUser();
            user.setUsername(userName);
            user.setPassword(password);
            synchronized (this) {
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null){
                            LoginActivity activity = (LoginActivity)getActivity();
                            assert activity != null;
                            activity.switchFragment(LoginFragmentCreator.SING_IN_FRAGMENT);
                            Toast.makeText(getContext(), "注册成功,请到登陆页面登录", Toast.LENGTH_SHORT).show();
                        }else {
                            int errorCode = e.getErrorCode();
                            if (errorCode == 202){
                                Toast.makeText(getContext(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
                                mNameTv.setText("");
                            }
                            Log.d(TAG, "error: "+e.getMessage());
                            Log.d(TAG, "errorCode: "+errorCode);
                        }
                    }
                });
            }
        }else{
            Toast.makeText(getContext(), "上面三项不能为空", Toast.LENGTH_SHORT).show();
        }
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
        mNameTv = view.findViewById(R.id.sign_up_user_nameTv);
        mPasswordTv = view.findViewById(R.id.sign_up_passwordTv);
        mSignUpBt = view.findViewById(R.id.sign_up_signUpBt);
    }
}
