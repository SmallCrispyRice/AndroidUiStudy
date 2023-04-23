package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androiduistudy.R;


public class FlyView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int viewHeight, viewWidth;
    private int imgHeight, imgWidth;
    private int left, top;

    public FlyView(Context context) {
        super(context);
        init(context);
    }

    public FlyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.fly);
        imgHeight = mBitmap.getHeight();
        imgWidth = mBitmap.getWidth();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        left = w / 2 - imgHeight / 2;
        top = h / 2 - imgWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, left, top, mPaint);
    }

    /** 移动图片 */
    public void move(float x, float y) {
        left += x;
        top += y;
        if (left < 0) {
            left = 0;
        }else if (left > viewWidth - imgWidth) {
            left = viewWidth - imgWidth;
        }

        if (top < 0) {
            top = 0;
        } else if (top > viewHeight - imgHeight) {
            top = viewHeight - imgHeight;
        }
        postInvalidate();
    }
}
