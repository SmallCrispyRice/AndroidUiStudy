package com.example.androiduistudy.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androiduistudy.R;

public class RadarMapLayout extends FrameLayout {
    private RadarView radarView;
    private ImageView imgNavDirection;
    ObjectAnimator imgAnimator;

    public RadarMapLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RadarMapLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarMapLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.radar_map_layout, this);
        radarView = view.findViewById(R.id.view_radar);
        imgNavDirection = view.findViewById(R.id.img_nav_direction);
    }

    public void setDegree(float degree) {
        radarView.setDegree(degree);
    }

    public void setNavDirectionDegree(float degree) {
        if (imgAnimator == null || !imgAnimator.isRunning()) {
            imgAnimator = ObjectAnimator.ofFloat(imgNavDirection, "rotation", degree, -degree*2,degree);
            imgAnimator.setDuration(100);
            imgAnimator.start();
        }
    }

    public void setLatLng(double lon1,double lat1, double lon2, double lat2) {
        radarView.setLatLng(lon1,lat1,lon2,lat2);
    }
}
