package com.telecom.touchscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleGestureDetectorActivity extends AppCompatActivity {
    private ImageView mGestureImageView;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_gesture_detector);
        mGestureImageView = (ImageView) findViewById(R.id.gesture_image_view);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                System.out.println("onScale()");
                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(0.1f,Math.min(mScaleFactor,5.0f));
                mGestureImageView.setScaleX(mScaleFactor);
                mGestureImageView.setScaleY(mScaleFactor);
                mGestureImageView.invalidate();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
