package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiduistudy.util.CustomLog;
import com.example.androiduistudy.util.DensityUtil;


public class IndexRecyclerView extends RecyclerView {
    private final static String TAG = "IndexListView";
    private Paint bgPaint, textPaint, indexPaint;
    private int viewHeight, viewWidth;
    private boolean selected = false;
    private RectF visibleRecF;
    private String[] indexS = {"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private String selectedIndexStr = "";
    private int selectedIndex = 0;
    private SparseArray<RectF> indexRectFs = new SparseArray<RectF>();
    public IndexRecyclerView(@NonNull Context context) {
        super(context);
    }

    public IndexRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(Context context) {
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setColor(Color.parseColor("#FFFFFF"));

        textPaint = new Paint();
        textPaint.setTextSize(DensityUtil.sp2px(context, 12));
        textPaint.setColor(Color.parseColor("#5DA9FF"));

        indexPaint = new Paint();
        indexPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        indexPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewHeight = getHeight();
        viewWidth = getWidth();
        visibleRecF = new RectF(viewWidth * 0.9F, viewHeight * 0.2F, viewWidth * 0.98F, viewHeight * 0.8F);

        float itemHeight = viewHeight * 0.6F / indexS.length;
        for (int i = 0; i < indexS.length; i++) {
            indexRectFs.put(i, new RectF(viewWidth * 0.9F, viewHeight * 0.2F + itemHeight * i, viewWidth * 0.98F, viewHeight * 0.2F + itemHeight * (i + 1)));
        }
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = viewWidth * 0.94F;
        float startY = viewHeight * 0.2F;
        float indexTotalHeight = viewHeight * 0.6F;
        float indexHeight = indexTotalHeight / 26;
        if (selected) {
            canvas.drawRect(indexRectFs.get(selectedIndex), bgPaint);
        }

        for (int i = 0; i < indexS.length; i++) {
            String index = indexS[i];
            canvas.drawText(index, startX, startY + indexHeight * (i + 1), textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        RectF rectF = new RectF(x, y, x, y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (RectF.intersects(visibleRecF, rectF)) {
                    CustomLog.INSTANCE.d(TAG,"indexRectFs:"+indexRectFs);
                    CustomLog.INSTANCE.d(TAG,"rectF:"+rectF);
                    for (int i = 0; i < indexRectFs.size(); i++) {
                        if (RectF.intersects(indexRectFs.get(i), rectF)) {
                            CustomLog.INSTANCE.d(TAG,"indexS[i]:"+indexS[i]);
                            selectedIndexStr = indexS[i];
                            selectedIndex = i;
                            selected = true;
                            invalidate();
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                selected = false;
                selectedIndexStr = "";
                selectedIndex = 0;
                invalidate();
                break;
        }
        return true;
    }
}
