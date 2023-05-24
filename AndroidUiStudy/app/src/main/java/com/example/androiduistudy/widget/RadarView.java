package com.example.androiduistudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androiduistudy.util.CustomLog;
import com.example.androiduistudy.util.DensityUtil;
import com.example.androiduistudy.util.GeoUtils;

public class RadarView extends View {
    private final int CIRCLE_NUM = 5;//绘制圆个数
    private final int LINE_WIDTH = 4;
    private final int MAP_MAX = 5;//雷达图最大显示范围，单位km，指的是雷达图半径对应多少km
    private Paint linePaint, bgPaint, pointPaint;
    private int defaultWidth = -1, defaultHeight = -1;
    private int viewWidth = -1, viewHeight = -1;
    private int radiusUnit = -1;//圆等分时候，单位半径，及最小圆半径
    private int centerX, centerY;
    private float length = 0f;
    private float pointX = 0f,pointY = 0f;
    private float degree;

    public RadarView(Context context) {
        super(context);
        init(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#919191"));
        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setStyle(Paint.Style.STROKE);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#FF666666"));
        bgPaint.setStyle(Paint.Style.FILL);

        pointPaint = new Paint();
        pointPaint.setColor(Color.parseColor("#919191"));
        pointPaint.setStyle(Paint.Style.FILL);

        defaultWidth = DensityUtil.dp2px(context, 200);
        defaultHeight = DensityUtil.dp2px(context, 200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = defaultWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = defaultHeight;
        }
        width = Math.min(width, height);
        height = width;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        radiusUnit = viewWidth / 2 / CIRCLE_NUM;
        centerX = viewWidth / 2;
        centerY = viewHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制雷达图线
        canvas.drawCircle(centerX, centerY, radiusUnit * CIRCLE_NUM, bgPaint);
        for (int i = 1; i <= CIRCLE_NUM; i++) {
            canvas.drawCircle(centerX, centerY, radiusUnit * i - LINE_WIDTH/2, linePaint);
        }
        canvas.drawLine(centerX, 0, centerX, viewHeight, linePaint);
        canvas.drawLine(0, centerY, viewWidth, centerY, linePaint);

        canvas.save();
        //变为数学坐标系
        canvas.translate(centerX,centerY);
        canvas.rotate(180);
        canvas.scale(-1,1);
        canvas.rotate(degree,0,0);//手机转动时候，点的位置
        //飞机点绘制
        canvas.drawCircle(pointX,pointY,15,pointPaint);
        canvas.restore();
    }

    /** 点旋转角度 */
    public void setDegree(float degree){
        this.degree = degree;
        postInvalidate();
    }

    /** (lon1,lat1)手机经纬度 (lon2,lat2)飞机经纬度*/
    public void setLatLng(double lon1,double lat1, double lon2, double lat2) {
        double distance = GeoUtils.calculateDistance(lon1,lat1,lon2,lat2);
        length = (float) (distance / MAP_MAX * viewWidth / 2f);//按照km为单位换算  一格1km
//        length = length * 100;//一格10m，范围较近时候用
        CustomLog.INSTANCE.d("distance:"+distance*1000);
        CustomLog.INSTANCE.d("r:"+ length);
        GeoUtils.MyLatLng point1 = new GeoUtils.MyLatLng(lon1,lat1);
        GeoUtils.MyLatLng point2 = new GeoUtils.MyLatLng(lon2,lat2);
        float angle = (float) GeoUtils.getAngle(point1,point2);
        pointX = length * (float) Math.sin(angle);
        pointY = length * (float) Math.cos(angle);
        postInvalidate();
    }
}
