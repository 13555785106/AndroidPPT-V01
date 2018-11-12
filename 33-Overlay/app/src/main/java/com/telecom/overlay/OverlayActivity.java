package com.telecom.overlay;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class OverlayActivity extends AppCompatActivity implements View.OnTouchListener {

    private RadioGroup mOptions;

    private ArrayList<Drawable> mMarkers;
    private Drawable mTrackingMarker;
    private Point mTrackingPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        //接收触摸事件在视图上进行绘画
        findViewById(R.id.textview).setOnTouchListener(this);
        mOptions = (RadioGroup) findViewById(R.id.container_options);
        mMarkers = new ArrayList<Drawable>();
    }

    /*
     * 处理触摸的事件.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (mOptions.getCheckedRadioButtonId()) {
            case R.id.option_box:
                handleEvent(R.id.option_box, v, event);
                break;
            case R.id.option_arrow:
                handleEvent(R.id.option_arrow, v, event);
                break;
            default:
                return false;
        }
        return true;
    }

    /*
     * 如果用户选择画一个盒子的触摸事件
     */
    private void handleEvent(int optionId, View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable current = markerAt(x, y);
                if (current == null) {
                    //为新的触摸添加一个新的标记
                    switch (optionId) {
                        case R.id.option_box:
                            mTrackingMarker = addBox(v, x, y);
                            mTrackingPoint = new Point(x, y);
                            break;
                        case R.id.option_arrow:
                            mTrackingMarker = addFlag(v, x, y);
                            break;
                    }
                } else {
                    //移除已经存在的标记
                    removeMarker(v, current);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //更新当前的标记让其移动起来
                if (mTrackingMarker != null) {
                    switch (optionId) {
                        case R.id.option_box:
                            resizeBox(v, mTrackingMarker, mTrackingPoint, x, y);
                            break;
                        case R.id.option_arrow:
                            offsetFlag(v, mTrackingMarker, x, y);
                            break;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手势终止清除状态
                mTrackingMarker = null;
                mTrackingPoint = null;
                break;
        }
    }

    /*
     * 在给定坐标添加一个新的大小可变的盒子
     */
    private Drawable addBox(View v, int x, int y) {
        Drawable box = getResources().getDrawable(R.drawable.box);
        //在触摸点创建一个尺寸为0的盒子
        Rect bounds = new Rect(x, y, x, y);
        box.setBounds(bounds);
        //添加到浮层
        mMarkers.add(box);
        v.getOverlay().add(box);
        return box;
    }

    /*
     * 基于给定坐标点改变盒子的大小
     */
    private void resizeBox(View v, Drawable target, Point trackingPoint, int x, int y) {
        Rect bounds = new Rect(target.getBounds());
        // 根据触摸点与起始点的位置更新边界
        if (x < trackingPoint.x) {
            bounds.left = x;
        } else {
            bounds.right = x;
        }
        if (y < trackingPoint.y) {
            bounds.top = y;
        } else {
            bounds.bottom = y;
        }
        //更新边界并重绘
        target.setBounds(bounds);
        v.invalidate();
    }

    /*
     * 在给定坐标添加一个新的指示标记
     */
    private Drawable addFlag(View v, int x, int y) {
        //创建一个新的指示标记绘画
        Drawable marker = getResources().getDrawable(R.drawable.flag_arrow, null);

        //创建一个匹配图像的边界
        Rect bounds = new Rect(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        //以标识底部中心点作为定位点
        bounds.offset(x - (bounds.width() / 2), y - bounds.height());
        marker.setBounds(bounds);
        //添加到浮层
        mMarkers.add(marker);
        v.getOverlay().add(marker);

        return marker;
    }

    /*
     * 更新一个标识标记的位置
     */
    private void offsetFlag(View v, Drawable marker, int x, int y) {
        Rect bounds = new Rect(marker.getBounds());
        //移动边界到新的坐标点
        bounds.offset(x - bounds.left - (bounds.width() / 2), y - bounds.top - bounds.height());
        //跟新并重绘
        marker.setBounds(bounds);
        v.invalidate();
    }

    /*
     * 移除选择的标记
     */
    private void removeMarker(View v, Drawable marker) {
        mMarkers.remove(marker);
        v.getOverlay().remove(marker);
    }

    /*
     * 在给定坐标点找到第一个标记
     */
    private Drawable markerAt(int x, int y) {
        //返回给定点的第一个标记
        for (Drawable marker : mMarkers) {
            if (marker.getBounds().contains(x, y)) {
                return marker;
            }
        }

        return null;
    }

}
