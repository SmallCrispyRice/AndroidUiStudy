package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androiduistudy.util.DensityUtil;



public class RockerView extends View {
    private final int VELOCITY = 40;//飞机速度

    private Paint smallCirclePaint;//小圆画笔
    private Paint bigCirclePaint;//大圆画笔
    private Paint sideCirclePaint;//大圆边框画笔
    private Paint arcPaint;//圆弧画布
    private int smallCenterX = -1, smallCenterY = -1;//绘制小圆圆心 x,y坐标
    private int bigCenterX = -1,bigCenterY = -1;//绘制大圆圆心 x,y坐标
    private int touchX = -1, touchY = -1;//触摸点 x,y坐标
    private float bigRadiusProportion = 69F / 110F;//大圆半径占view一半宽度的比例 用于获取大圆半径
    private float smallRadiusProportion = 4F / 11F;//小圆半径占view一半宽度的比例
    private float bigRadius = -1;//大圆半径
    private float smallRadius = -1;//小圆半径
    private double distance = -1; //手指按压点与大圆圆心的距离
    private double radian = -1;//弧度
    private float angle = -1;//度数 -180~180
    private int viewHeight,viewWidth;
    private int defaultViewHeight, defaultViewWidth;
    private RectF arcRect = new RectF();//绘制蓝色圆弧用到矩形
    private int drawArcAngle = 90;//圆弧绘制度数
    private int arcOffsetAngle = -45;//圆弧偏移度数
    private int drawTime = 16;//告诉flyView重绘的时间间隔 这里是16ms一次
    private boolean isBigCircleOut = false;//触摸点在大圆外

    private boolean isStart = false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStart){
                getFlyOffset();
                mHandler.postDelayed(this,drawTime);
            }
        }
    };

    public RockerView(Context context) {
        super(context);
        init(context);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        defaultViewWidth = DensityUtil.dp2px(context,220);
        defaultViewHeight = DensityUtil.dp2px(context,220);

        bigCirclePaint = new Paint();
        bigCirclePaint.setStyle(Paint.Style.FILL);
        bigCirclePaint.setStrokeWidth(5);
        bigCirclePaint.setColor(Color.parseColor("#1AFFFFFF"));
        bigCirclePaint.setAntiAlias(true);

        smallCirclePaint = new Paint();
        smallCirclePaint.setStyle(Paint.Style.FILL);
        smallCirclePaint.setStrokeWidth(5);
        smallCirclePaint.setColor(Color.parseColor("#4DFFFFFF"));
        smallCirclePaint.setAntiAlias(true);

        sideCirclePaint = new Paint();
        sideCirclePaint.setStyle(Paint.Style.STROKE);
        sideCirclePaint.setStrokeWidth(DensityUtil.dp2px(context, 1));
        sideCirclePaint.setColor(Color.parseColor("#33FFFFFF"));
        sideCirclePaint.setAntiAlias(true);

        arcPaint = new Paint();
        arcPaint.setColor(Color.parseColor("#FF5DA9FF"));
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(5);
        arcPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取视图的宽高的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width,height;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            width = defaultViewWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            height = defaultViewHeight;
        }
        width = Math.min(width,height);
        height = width;
        //设置视图的宽度和高度
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        bigCenterX = getWidth() / 2;
        bigCenterY = getHeight() / 2;
        smallCenterX = bigCenterX;
        smallCenterY = bigCenterY;

        bigRadius = bigRadiusProportion * Math.min(bigCenterX, bigCenterY);
        smallRadius = smallRadiusProportion * Math.min(bigCenterX, bigCenterY);

        arcRect.set(bigCenterX-bigRadius,bigCenterY-bigRadius,bigCenterX+bigRadius,bigCenterY+bigRadius);
        viewHeight = getHeight();
        viewWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(bigCenterX, bigCenterY, bigRadius, bigCirclePaint);
        canvas.drawCircle(smallCenterX, smallCenterY, smallRadius, smallCirclePaint);
        canvas.drawCircle(bigCenterX, bigCenterY, bigRadius, sideCirclePaint);

        if (isBigCircleOut) {
            canvas.drawArc(arcRect,angle+arcOffsetAngle,drawArcAngle,false,arcPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                initAngle();
                getFlyOffset();
                if (isStart) {
                    isStart = false;
                    mHandler.removeCallbacks(mRunnable);
                }
                isStart = true;
                mHandler.postDelayed(mRunnable,drawTime);
                break;
            case MotionEvent.ACTION_UP:
                smallCenterX = bigCenterX;
                smallCenterY = bigCenterY;
                isBigCircleOut = false;
                if (isStart) {
                    mHandler.removeCallbacks(mRunnable);//有问题
                    isStart = false;
                }
                break;
        }
        postInvalidate();
        return true;
    }

    /** 计算夹角度数，并实现小圆圆心最多至大圆边上 */
    private void initAngle() {
        radian = Math.atan2((touchY - bigCenterY), (touchX - bigCenterX));
        angle = (float) (radian * (180 / Math.PI));//范围-180-180
        isBigCircleOut = false;
        if (bigCenterX != -1 && bigCenterY != -1) {//大圆中心xy已赋值
            double rxr = (double) Math.pow(touchX - bigCenterX, 2) + Math.pow(touchY - bigCenterY, 2);
            distance = Math.sqrt(rxr);//手点击点距离大圆圆心距离
            smallCenterX = touchX;
            smallCenterY = touchY;
            if (distance > bigRadius) {//距离大于半圆半径时，固定小圆圆心在大圆边缘上
                smallCenterX = (int) (bigRadius / distance * (touchX - bigCenterX)) + bigCenterX;
                smallCenterY = (int) (bigRadius / distance * (touchY - bigCenterY)) + bigCenterX;
                isBigCircleOut = true;
            }
        }
    }

    /** 获取飞行偏移量 */
    private void getFlyOffset() {
        float x = (smallCenterX - bigCenterX) * 1.0f / viewWidth * VELOCITY;
        float y = (smallCenterY - bigCenterY) * 1.0f / viewHeight * VELOCITY;
        onRockerListener.getDate(this, x, y);
    }

    /**
     * pX,pY为手指按点坐标减view的坐标
     */
    public interface OnRockerListener {
        public void getDate(RockerView rocker, final float pX, final float pY);
    }
    private OnRockerListener onRockerListener;
    public void getDate(final OnRockerListener onRockerListener) {
        this.onRockerListener = onRockerListener;
    }
}
