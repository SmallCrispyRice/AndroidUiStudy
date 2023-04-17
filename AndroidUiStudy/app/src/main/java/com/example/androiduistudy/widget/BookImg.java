package com.example.androiduistudy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androiduistudy.util.DensityUtil;

public class BookImg extends androidx.appcompat.widget.AppCompatImageView {
    private int defHeight, defWidth;
    public BookImg(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BookImg(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BookImg(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //在写的时候发现，普通的img不能恰好填充网格布局，往往会多出或者少一部分
        //下面是获取屏幕宽度减去间隔除以3  3是一行3个img与前面对其，宽度有了在算出图片的高度， * 762 / 640 是图片的高和宽的像素，按照图片比例来算的
        //更正确方式应该是通过bitmap获取，这里偷懒了
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = manager.getDefaultDisplay().getWidth();
        defWidth = (screenWidth - DensityUtil.dp2px(context,40))/3;
        defHeight = defWidth * 762 / 640;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(defWidth, defHeight);
//        }else if (widthMode == MeasureSpec.AT_MOST){
//            setMeasuredDimension(defWidth,heightSize);
//        }else if (heightMode == MeasureSpec.AT_MOST){
//            setMeasuredDimension(widthSize, defHeight);
//        }else {
//            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
//        }
        //不知道为什么 widthMeasureSpec是很大的负数 heightMeasureSpec是 0
        //不能判断出warp和match，因此这里只能写死了
        setMeasuredDimension(defWidth, defHeight);
    }
}
