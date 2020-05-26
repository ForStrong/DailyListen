package com.ianf.dailylisten.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.R;
import com.ianf.dailylisten.adapters.IndicatorAdapter;
import com.ianf.dailylisten.adapters.LoginViewPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private  ViewPager mLoginViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        MagicIndicator indicator = findViewById(R.id.login_indicator);
        mLoginViewPager = findViewById(R.id.login_viewPager);
        //初始化indicator
        indicator.setBackgroundColor(getResources().getColor(R.color.main_color,null));
        //初始化导航栏
        CommonNavigator commonNavigator = new CommonNavigator(this);
        List<String> pageTitles = Arrays.asList(getResources().getStringArray(R.array.login_title));
        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, mLoginViewPager,pageTitles);
        commonNavigator.setAdapter(indicatorAdapter);
        commonNavigator.setAdjustMode(true);
        //indicator设置导航栏
        indicator.setNavigator(commonNavigator);
        //初始化ViewPager
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginViewPagerAdapter adapter = new LoginViewPagerAdapter(fragmentManager);
        mLoginViewPager.setAdapter(adapter);
        //indicator和ViewPager绑定在一起
        ViewPagerHelper.bind(indicator, mLoginViewPager);
    }

    public  void switchFragment(int index){
        mLoginViewPager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewPager = null;
    }
}
