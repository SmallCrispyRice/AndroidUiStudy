package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {
    private Paint redPaint, greenPaint, blackPaint;
    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(5);


        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStrokeWidth(5);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        float centerX = getWidth()/2;
        float centerY = getHeight()/2;
        canvas.drawLine(0,centerY,getWidth(),centerY, blackPaint);
        canvas.drawLine(centerX,0,centerX,getHeight(), blackPaint);
        canvas.translate(centerX,centerY);
        canvas.rotate(180);

        canvas.scale(-1,1);

        canvas.drawLine(0,0,0,50, redPaint);
        canvas.rotate(30,0,0);
        canvas.drawLine(0,0,0,50, greenPaint);
    }
}
