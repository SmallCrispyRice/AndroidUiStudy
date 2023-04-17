package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androiduistudy.util.CustomLog;
import com.example.androiduistudy.util.DensityUtil;
import com.example.androiduistudy.util.UIUtils;


/**
 * 索引view，需要优化尤其是onDraw
 * 通过接口，把松手后的索引值传递给外层
 */
public class IndexListView extends View {
    private final static String TAG = "IndexListView";
    private Paint bgPaint, textPaint, indexPaint;
    private int defaultViewHeight, defaultViewWidth;
    private boolean selected = false;
    private String[] indexS = {"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private int position = -1;
    private SparseArray<RectF> indexRectFs = new SparseArray<RectF>();
    private float indexHeight;//单个字母项高度

    public IndexListView(Context context) {
        super(context);
        init(context);
    }

    public IndexListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndexListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        defaultViewWidth = (int) (UIUtils.getScreenWidth(context) * 0.1);
        defaultViewHeight = (int) (UIUtils.getScreenHeight(context) * 0.6);
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setColor(Color.GREEN);

        textPaint = new Paint();
        textPaint.setTextSize(DensityUtil.sp2px(context, 12));
        textPaint.setColor(Color.parseColor("#5DA9FF"));

        indexPaint = new Paint();
        indexPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        indexPaint.setColor(Color.WHITE);
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
        CustomLog.INSTANCE.d("widthSize:"+widthSize);
        CustomLog.INSTANCE.d("getScreenWidth:"+defaultViewWidth*10);
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else if (widthMode == MeasureSpec.AT_MOST){
            width = Math.min(defaultViewWidth,widthSize);
        }else {
            width = defaultViewWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else if (heightMode == MeasureSpec.AT_MOST){
            height = Math.min(defaultViewHeight,heightSize);
        }else {
            height = defaultViewHeight;
        }
        //设置视图的宽度和高度
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float itemHeight = defaultViewHeight / indexS.length;
        for (int i = 0; i < indexS.length; i++) {
            indexRectFs.put(i, new RectF(0,itemHeight * i, defaultViewWidth, itemHeight * (i + 1)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        indexHeight = defaultViewHeight / indexS.length;
        if (selected) {
            canvas.drawRect(indexRectFs.get(position), bgPaint);
        }

        for (int i = 0; i < indexS.length; i++) {
            String index = indexS[i];
            canvas.drawText(index, getWidth()/3,indexHeight * (i + 1), textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        RectF rectF = new RectF(0, y, defaultViewWidth, y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < indexRectFs.size(); i++) {
                    if (RectF.intersects(indexRectFs.get(i), rectF)) {
                        if (position!= i){//只有当经过的位置与上次不同的时候,才重绘,减少消耗
                            position = i;
                            selected = true;
                            invalidate();
                            if (listener!=null){
                                listener.onPosition(position);//逻辑代码中发送数据
                            }
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                position = -1;
                selected = false;
                if (listener!=null){
                    listener.onPosition(-1);//逻辑代码中发送数据
                }
                invalidate();
                break;
        }
        return true;
    }

    public interface OnPositionListener{
        void onPosition(int position);
    }
    private OnPositionListener listener;
    public void setOnPositionListener(OnPositionListener listener){
        this.listener = listener;
    }
}