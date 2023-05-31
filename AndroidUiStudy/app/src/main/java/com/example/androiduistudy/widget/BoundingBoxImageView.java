package com.example.androiduistudy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.androiduistudy.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 算法标注框
 */
public class BoundingBoxImageView extends androidx.appcompat.widget.AppCompatImageView {
    private final int NONE = 0, CIRCLE = 1, RECT = 2, OUT_RECT = 3;
    private Paint boxPaint, broadsidePaint, cornerPaint, decorationPaint;
    private float downX = -1f, downY = -1f;//手指按下
    private float lastX = -1f, lastY = -1f;//上一个xy
    private RectF boxRectF = new RectF();//算法框
    private RectF lastBoxRect = new RectF();//上次手指抬起时算法框
    private RectF downRectF = new RectF();//手指按下点Rect
    private RectF sideBoxRectF = new RectF();//用于拖拽算法框边缘的矩形
    private RectF leftRectF = new RectF(), leftTopRectF = new RectF(), topRectF = new RectF(), rightTopRectF = new RectF(), rightRectF = new RectF(), rightBottomRectF = new RectF(), bottomRectF = new RectF(), leftBottomRectF = new RectF();
    private RectF dragRectF = new RectF();//拖拽区域Rect
    private boolean boxRectConfirm = false;//标注框确认 当第一次手指松开时候该值为true，确定标注框已经有了
    private boolean changeBoxSide = false;//标注框形状改变
    private boolean showDragWidth = false;//展示拖拽区域
    private float boxLineWidth = 5;//标注框线宽
    private float dragWidth = 30;//拖拽区域宽度
    private float decorationLineLength = 80;//装饰线长度
    private float decorationLineWidth = 10;//装饰线宽度

    private int imgWidth, imgHeight;
    private float viewWidth, viewHeight;
    private float leftPercent, topPercent, rightPercent, bottomPercent;
    private int[] coordinates = new int[4];//保存标注框左上角与右下角点左边，startX,startY,endX,endY
    private Point startPoint = new Point(),endPoint = new Point();
    private int boxColor = Color.parseColor("#99129823");
    private int decorationColor = Color.parseColor("#5DA9FF");
    private int decorationType = NONE;


    private Path leftTopPath = new Path(), rightTopPath = new Path(), rightBottomPath = new Path(), leftBottomPath = new Path();


    public BoundingBoxImageView(Context context) {
        super(context);
    }

    public BoundingBoxImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BoundingBoxImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BoundingBoxImageView);
        showDragWidth = typedArray.getBoolean(R.styleable.BoundingBoxImageView_show_drag_width, showDragWidth);
        dragWidth = typedArray.getDimension(R.styleable.BoundingBoxImageView_drag_width, dragWidth);

        boxLineWidth = typedArray.getDimension(R.styleable.BoundingBoxImageView_box_line_width, boxLineWidth);
        boxColor = typedArray.getColor(R.styleable.BoundingBoxImageView_box_color, boxColor);


        decorationLineLength = typedArray.getDimension(R.styleable.BoundingBoxImageView_decoration_line_length, decorationLineLength);
        decorationLineWidth = typedArray.getDimension(R.styleable.BoundingBoxImageView_decoration_line_width, decorationLineWidth);
        decorationColor = typedArray.getColor(R.styleable.BoundingBoxImageView_decoration_color, decorationColor);
        decorationType = typedArray.getInt(R.styleable.BoundingBoxImageView_decoration_type,decorationType);

        typedArray.recycle();//释放，避免内存泄露

        boxPaint = new Paint();
        boxPaint.setColor(boxColor);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(boxLineWidth);

        decorationPaint = new Paint();
        decorationPaint.setColor(decorationColor);
        decorationPaint.setStyle(Paint.Style.STROKE);
        decorationPaint.setStrokeWidth(decorationLineWidth);
        decorationPaint.setAntiAlias(true);//开启抗锯齿

        broadsidePaint = new Paint();
        broadsidePaint.setColor(Color.parseColor("#99345212"));
        broadsidePaint.setStrokeWidth(dragWidth);

        cornerPaint = new Paint();
        cornerPaint.setColor(Color.parseColor("#99876534"));
        cornerPaint.setStrokeWidth(dragWidth);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                imgHeight = bitmap.getHeight();
                imgWidth = bitmap.getWidth();
            }
        }
        viewWidth = getWidth();
        viewHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (boxRectF != null) {
            canvas.drawRect(boxRectF, boxPaint);
            //边框装饰
            drawDecoration(canvas, decorationType);
            //开发测试使用
            if (showDragWidth) {
                canvas.drawRect(leftRectF, broadsidePaint);
                canvas.drawRect(topRectF, broadsidePaint);
                canvas.drawRect(rightRectF, broadsidePaint);
                canvas.drawRect(bottomRectF, broadsidePaint);

                canvas.drawRect(leftTopRectF, cornerPaint);
                canvas.drawRect(rightTopRectF, cornerPaint);
                canvas.drawRect(leftBottomRectF, cornerPaint);
                canvas.drawRect(rightBottomRectF, cornerPaint);
            }
        }
    }

    private void drawDecoration(Canvas canvas, int type) {
        //矩形确认后才绘制装饰，好处是，避免矩形太小时候导致装饰挤在一起
        if (boxRectConfirm) {
            if (type == NONE) {
                return;
            } else if (type == RECT) {
                //左上右下
                canvas.drawLine(boxRectF.left, (boxRectF.top + boxRectF.bottom) / 2 - decorationLineLength / 2, boxRectF.left, (boxRectF.top + boxRectF.bottom) / 2 + decorationLineLength / 2, decorationPaint);
                canvas.drawLine((boxRectF.left + boxRectF.right) / 2 - decorationLineLength / 2, boxRectF.top, (boxRectF.left + boxRectF.right) / 2 + decorationLineLength / 2, boxRectF.top, decorationPaint);
                canvas.drawLine(boxRectF.right, (boxRectF.top + boxRectF.bottom) / 2 - decorationLineLength / 2, boxRectF.right, (boxRectF.top + boxRectF.bottom) / 2 + decorationLineLength / 2, decorationPaint);
                canvas.drawLine((boxRectF.left + boxRectF.right) / 2 - decorationLineLength / 2, boxRectF.bottom, (boxRectF.left + boxRectF.right) / 2 + decorationLineLength / 2, boxRectF.bottom, decorationPaint);

                leftTopPath.reset();
                leftTopPath.moveTo(boxRectF.left, boxRectF.top + decorationLineLength);
                leftTopPath.lineTo(boxRectF.left, boxRectF.top);
                leftTopPath.lineTo(boxRectF.left + decorationLineLength, boxRectF.top);
                canvas.drawPath(leftTopPath, decorationPaint);

                rightTopPath.reset();
                rightTopPath.moveTo(boxRectF.right - decorationLineLength, boxRectF.top);
                rightTopPath.lineTo(boxRectF.right, boxRectF.top);
                rightTopPath.lineTo(boxRectF.right, boxRectF.top + decorationLineLength);
                canvas.drawPath(rightTopPath, decorationPaint);

                rightBottomPath.reset();
                rightBottomPath.moveTo(boxRectF.right, boxRectF.bottom - decorationLineLength);
                rightBottomPath.lineTo(boxRectF.right, boxRectF.bottom);
                rightBottomPath.lineTo(boxRectF.right - decorationLineLength, boxRectF.bottom);
                canvas.drawPath(rightBottomPath, decorationPaint);

                leftBottomPath.reset();
                leftBottomPath.moveTo(boxRectF.left + decorationLineLength, boxRectF.bottom);
                leftBottomPath.lineTo(boxRectF.left, boxRectF.bottom);
                leftBottomPath.lineTo(boxRectF.left, boxRectF.bottom - decorationLineLength);
                canvas.drawPath(leftBottomPath, decorationPaint);
            } else if (type == CIRCLE) {
                //角装饰 左上 右上 右下 左下
                canvas.drawCircle(boxRectF.left, boxRectF.top, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle(boxRectF.right, boxRectF.top, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle(boxRectF.right, boxRectF.bottom, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle(boxRectF.left, boxRectF.bottom, decorationLineWidth / 2, decorationPaint);
                //边装饰 左上右下
                canvas.drawCircle(boxRectF.left, (boxRectF.top + boxRectF.bottom) / 2, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle((boxRectF.left + boxRectF.right) / 2, boxRectF.top, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle(boxRectF.right, (boxRectF.top + boxRectF.bottom) / 2, decorationLineWidth / 2, decorationPaint);
                canvas.drawCircle((boxRectF.left + boxRectF.right) / 2, boxRectF.bottom, decorationLineWidth / 2, decorationPaint);
            } else if (type == OUT_RECT) {
                canvas.drawLine(boxRectF.left - decorationLineWidth / 2, (boxRectF.top + boxRectF.bottom) / 2 - decorationLineLength / 2, boxRectF.left - decorationLineWidth / 2, (boxRectF.top + boxRectF.bottom) / 2 + decorationLineLength / 2, decorationPaint);
                canvas.drawLine((boxRectF.left + boxRectF.right) / 2 - decorationLineLength / 2, boxRectF.top - decorationLineWidth / 2, (boxRectF.left + boxRectF.right) / 2 + decorationLineLength / 2, boxRectF.top - decorationLineWidth / 2, decorationPaint);
                canvas.drawLine(boxRectF.right + decorationLineWidth / 2, (boxRectF.top + boxRectF.bottom) / 2 - decorationLineLength / 2, boxRectF.right + decorationLineWidth / 2, (boxRectF.top + boxRectF.bottom) / 2 + decorationLineLength / 2, decorationPaint);
                canvas.drawLine((boxRectF.left + boxRectF.right) / 2 - decorationLineLength / 2, boxRectF.bottom + decorationLineWidth / 2, (boxRectF.left + boxRectF.right) / 2 + decorationLineLength / 2, boxRectF.bottom + decorationLineWidth / 2, decorationPaint);

                leftTopPath.reset();
                leftTopPath.moveTo(boxRectF.left - decorationLineWidth / 2, boxRectF.top + decorationLineLength);
                leftTopPath.lineTo(boxRectF.left - decorationLineWidth / 2, boxRectF.top - decorationLineWidth / 2);
                leftTopPath.lineTo(boxRectF.left + decorationLineLength, boxRectF.top - decorationLineWidth / 2);
                canvas.drawPath(leftTopPath, decorationPaint);

                rightTopPath.reset();
                rightTopPath.moveTo(boxRectF.right - decorationLineLength, boxRectF.top - decorationLineWidth / 2);
                rightTopPath.lineTo(boxRectF.right + decorationLineWidth / 2, boxRectF.top - decorationLineWidth / 2);
                rightTopPath.lineTo(boxRectF.right + decorationLineWidth / 2, boxRectF.top + decorationLineLength);
                canvas.drawPath(rightTopPath, decorationPaint);

                rightBottomPath.reset();
                rightBottomPath.moveTo(boxRectF.right + decorationLineWidth / 2, boxRectF.bottom - decorationLineLength);
                rightBottomPath.lineTo(boxRectF.right + decorationLineWidth / 2, boxRectF.bottom + decorationLineWidth / 2);
                rightBottomPath.lineTo(boxRectF.right - decorationLineLength, boxRectF.bottom + decorationLineWidth / 2);
                canvas.drawPath(rightBottomPath, decorationPaint);

                leftBottomPath.reset();
                leftBottomPath.moveTo(boxRectF.left + decorationLineLength, boxRectF.bottom + decorationLineWidth / 2);
                leftBottomPath.lineTo(boxRectF.left - decorationLineWidth / 2, boxRectF.bottom + decorationLineWidth / 2);
                leftBottomPath.lineTo(boxRectF.left - decorationLineWidth / 2, boxRectF.bottom - decorationLineLength);
                canvas.drawPath(leftBottomPath, decorationPaint);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                //内部拦截 解决滑动冲突
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                downRectF.set(downX, downY, downX, downY);
                //表示可以改变矩形形状 得是矩形确认形状后
                if (sideBoxRectF.contains(downRectF) && !dragRectF.contains(downRectF) && boxRectConfirm) {
                    changeBoxSide = true;
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = x - lastX;
                float offsetY = y - lastY;
                if (!boxRectConfirm) {//矩形确定 第一次松手后 确认矩形 因为new Rect()的点都是0，必须记录下downX和后面手指移动的X
                    boxRectF.set(Math.min(downX, x), Math.min(downY, y), Math.max(downX, x), Math.max(downY, y));
                }

                //拖拽标注框 范围超出View时处理  改变框时候不触发下面条件
                if (dragRectF.contains(downRectF) && !changeBoxSide && boxRectConfirm) {//手指按下点，在上一个lastBoxRectF矩形内，才考虑执行下面的代码   因为boxRectF会变的，有可能导致变着变着boxRectF包含了downRectF
                    boxRectF.offset(offsetX, offsetY);
                }

                //拖动边缘改变View大小和形状
                //当左边框小到一定程度就不响应了
                if (changeBoxSide) {
                    //感觉需要对 矩形上下左右区域做下区分
                    if (leftRectF.contains(downRectF)) {//左区域
                        boxRectF.left += offsetX;
                    }else if (leftTopRectF.contains(downRectF) && !dragRectF.contains(downRectF)) {//左上区域 特殊在，点击位置不位于上一次的标注框内
                        boxRectF.left += offsetX;
                        boxRectF.top += offsetY;
                    }else if (topRectF.contains(downRectF)) {//上区域
                        boxRectF.top += offsetY;
                    }else if (rightTopRectF.contains(downRectF) && !dragRectF.contains(downRectF)) {
                        boxRectF.top += offsetY;
                        boxRectF.right += offsetX;
                    }else if (rightRectF.contains(downRectF)) {//右区域
                        boxRectF.right += offsetX;
                    }else if (rightBottomRectF.contains(downRectF) && !dragRectF.contains(downRectF)) {
                        boxRectF.right += offsetX;
                        boxRectF.bottom += offsetY;
                    }else if (bottomRectF.contains(downRectF)) {//下区域
                        boxRectF.bottom += offsetY;
                    }else if (leftBottomRectF.contains(downRectF) && !dragRectF.contains(downRectF)) {
                        boxRectF.left += offsetX;
                        boxRectF.bottom += offsetY;
                    }
                }

                //确认矩形 与 改变矩形大小都需要判断修改后的矩形是否超出View
                if (boxRectF.left < 0){
                    boxRectF.left = 0;
                }else if (boxRectF.left > viewWidth){
                    boxRectF.left = viewWidth;
                }
                if (boxRectF.top < 0){
                    boxRectF.top = 0;
                }else if (boxRectF.top > viewHeight){
                    boxRectF.top = viewHeight;
                }
                if (boxRectF.right < 0){
                    boxRectF.right = 0;
                }else if (boxRectF.right > viewWidth){
                    boxRectF.right = viewWidth;
                }
                if (boxRectF.bottom < 0){
                    boxRectF.bottom = 0;
                }else if (boxRectF.bottom > viewHeight){
                    boxRectF.bottom = viewHeight;
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                float left = Math.min(boxRectF.left, boxRectF.right);
                float right = Math.max(boxRectF.left, boxRectF.right);
                float top = Math.min(boxRectF.top, boxRectF.bottom);
                float bottom = Math.max(boxRectF.top, boxRectF.bottom);
                boxRectF.set(left,top,right,bottom);
                lastBoxRect.set(boxRectF);

                dragRectF.set(boxRectF.left + dragWidth, boxRectF.top + dragWidth, boxRectF.right - dragWidth, boxRectF.bottom - dragWidth);

                sideBoxRectF.set(boxRectF.left - dragWidth, boxRectF.top - dragWidth, boxRectF.right + dragWidth, boxRectF.bottom + dragWidth);

                leftRectF.set(boxRectF.left - dragWidth, boxRectF.top + dragWidth, boxRectF.left + dragWidth, boxRectF.bottom - dragWidth);

                leftTopRectF.set(boxRectF.left - dragWidth, boxRectF.top - dragWidth, boxRectF.left + dragWidth, boxRectF.top + dragWidth);

                topRectF.set(boxRectF.left + dragWidth, boxRectF.top - dragWidth, boxRectF.right - dragWidth, boxRectF.top + dragWidth);

                rightTopRectF.set(boxRectF.right - dragWidth, boxRectF.top - dragWidth, boxRectF.right + dragWidth, boxRectF.top + dragWidth);

                rightRectF.set(boxRectF.right - dragWidth, boxRectF.top + dragWidth, boxRectF.right + dragWidth, boxRectF.bottom - dragWidth);

                rightBottomRectF.set(boxRectF.right - dragWidth, boxRectF.bottom - dragWidth, boxRectF.right + dragWidth, boxRectF.bottom + dragWidth);

                bottomRectF.set(boxRectF.left + dragWidth, boxRectF.bottom - dragWidth, boxRectF.right - dragWidth, boxRectF.bottom + dragWidth);

                leftBottomRectF.set(boxRectF.left - dragWidth, boxRectF.bottom - dragWidth, boxRectF.left + dragWidth, boxRectF.bottom + dragWidth);

                //手指抬起时，计算点百分比位置
                leftPercent = boxRectF.left / viewWidth;
                topPercent = boxRectF.top / viewHeight;
                rightPercent = boxRectF.right / viewWidth;
                bottomPercent = boxRectF.bottom / viewHeight;

                coordinates[0] = (int) (imgWidth * leftPercent);
                coordinates[1] = (int) (imgHeight * topPercent);
                coordinates[2] = (int) (imgWidth * rightPercent);
                coordinates[3] = (int) (imgHeight * bottomPercent);

                boxRectConfirm = true;
                changeBoxSide = false;
                break;
        }
        postInvalidate();
        return true;
    }

    public int[] getCoordinates(){
        return coordinates;
    }

    public void clearBox() {
        boxRectF.set(0,0,0,0);
        Arrays.fill(coordinates,0);
        boxRectConfirm = false;
        postInvalidate();
    }

    public void setDecorationType(int decorationType){
        this.decorationType = decorationType;
        postInvalidate();
    }
}
