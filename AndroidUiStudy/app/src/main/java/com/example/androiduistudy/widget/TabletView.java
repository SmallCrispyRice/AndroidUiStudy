package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;

public class TabletView extends View {
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private int viewWidth,viewHeight;
    public TabletView(Context context) {
        super(context);
        init();
    }

    public TabletView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabletView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取View宽高
        viewWidth = w;
        viewHeight = h;
        mBitmap = Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                mBitmapCanvas.drawPath(mPath,mPaint);
                break;
        }
        postInvalidate();
        return true;
    }

    /** 清空面板 */
    public void clearTablet(){
        mPath.reset();
        mBitmap = Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
        postInvalidate();
    }

    public void saveToFile(String filePath){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            mBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
