package com.example.androiduistudy.ui.bookshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiduistudy.R;
import com.example.androiduistudy.util.DensityUtil;

/** 书架装饰类*/
public class BookShelfItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int paintWidth = 2;
    private int viewMargin = 4;//子item用于绘制的区域宽度 上下左右
    private Bitmap decorationBmp;//装饰图片
    private int row = 3;//recyclerView列数
    public BookShelfItemDecoration(Context context) {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paintWidth);
        decorationBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ok_press);
    }

    /** 绘制 */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i=0;i<childCount;i++){
            View child = parent.getChildAt(i);//判空，这里有可能为空
            if (child!=null){
                int startX = child.getLeft() - paintWidth;
                int startY = child.getTop() - paintWidth;
                int endX = child.getRight() + paintWidth;
                int endY = child.getBottom() + paintWidth;
                Rect rect = new Rect(startX,startY,endX,endY);
                c.drawRect(rect,paint);
            }
        }
    }


    /** 绘制在最上层 */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i=0;i<childCount;i++){
            View child = parent.getChildAt(i);
            if (child!=null && i%row==0){
                //如果让图片居中的话还得减去图片宽高的一半，绘制时候是从startX，startY点开始绘制图片左上角
                int startX = (child.getLeft()+child.getRight())/2 - decorationBmp.getWidth()/2;
                int startY = child.getTop() + child.getPaddingTop() - decorationBmp.getHeight()/2;
                c.drawBitmap(decorationBmp,startX,startY,paint);
            }
        }
    }

    /** 给item四周添加边距，给item添加边距后，可在onDraw方法在边距绘制了 */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom= DensityUtil.dp2px(view.getContext(), viewMargin);
        outRect.top= DensityUtil.dp2px(view.getContext(), viewMargin);
        outRect.left= DensityUtil.dp2px(view.getContext(), viewMargin);
        outRect.right= DensityUtil.dp2px(view.getContext(), viewMargin);
    }
}
