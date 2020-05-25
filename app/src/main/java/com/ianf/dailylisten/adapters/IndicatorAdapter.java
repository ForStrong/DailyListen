package com.ianf.dailylisten.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.ianf.dailylisten.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
*create by IANDF in 2020/4/21
 *lastTime:
 *@description: MagicIndicator的适配器，indicator根据adapter调节顶部item
 *@usage:
*/
public class IndicatorAdapter extends CommonNavigatorAdapter {
    private List<String> mList;
    private ViewPager mViewPager;
    public IndicatorAdapter(Context context, ViewPager viewPager,List<String> pageStrings) {
        mList = pageStrings;
        mViewPager = viewPager;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        //创建view
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        //设置一般情况下的颜色为灰色
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#aaffffff"));
        //设置选中情况下的颜色为黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
        //设置字体大小，单位sp
        colorTransitionPagerTitleView.setTextSize(18);
        colorTransitionPagerTitleView.setText(mList.get(index));
        colorTransitionPagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        linePagerIndicator.setColors(Color.parseColor("#ffffff"));
        return linePagerIndicator;
    }
}
