package com.ianf.dailylisten.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.ianf.dailylisten.R;

@SuppressLint("AppCompatCustomView")
public class LoadingImageView extends ImageView {
    private int mRotateDegree = 0;
    private boolean isRotate = true;

    public LoadingImageView(Context context) {
        this(context,null);
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置图标
        setImageResource(R.mipmap.loading);
    }
    //绑定到window上执行
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                mRotateDegree = (mRotateDegree + 30) % 360;
                //调用invalidate执行onDraw
                invalidate();
                //是否需要旋转
                if (isRotate){
                    postDelayed(this,100);
                }
            }
        });
    }
    //解绑window上执行
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //解绑，否则会一直转,postDelayed一致执行，消耗资源
        isRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*
            params:
            1.旋转的角度
            2.旋转的x坐标
            3.旋转的y坐标
         */
        canvas.rotate(mRotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
