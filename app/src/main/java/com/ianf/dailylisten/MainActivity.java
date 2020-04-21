package com.ianf.dailylisten;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.adapters.IndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MagicIndicator magicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        magicIndicator = findViewById(R.id.magic_indicator);
        ViewPager viewPager = findViewById(R.id.content_pager);
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        IndicatorAdapter adapter = new IndicatorAdapter(this);
        commonNavigator.setAdapter(adapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,viewPager);
    }
}
