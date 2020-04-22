package com.ianf.dailylisten;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.adapters.IndicatorAdapter;
import com.ianf.dailylisten.adapters.MainViewPagerAdapter;

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
        //init indicator
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自定义Indicator适配器
        IndicatorAdapter adapter = new IndicatorAdapter(this,viewPager);
        //给CommonNavigator设置适配器
        commonNavigator.setAdapter(adapter);
        //根据控件宽度自动调节Item的位置,平分宽度
        commonNavigator.setAdjustMode(true);
        //给Indicator设置导航栏
        magicIndicator.setNavigator(commonNavigator);

        //init viewPager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //自定义ViewPager适配器
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(fragmentManager);
        //给ViewPager设置适配器
        viewPager.setAdapter(viewPagerAdapter);

        //bind viewPager and indicator
        ViewPagerHelper.bind(magicIndicator,viewPager);


    }
}
