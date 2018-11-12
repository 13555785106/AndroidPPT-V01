package com.telecom.touchscreen;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GestureDetectorActivity extends AppCompatActivity {
    TextView mMessageTextView;
    StringBuilder mStringBuilder = new StringBuilder();
    GestureDetectorCompat mGestureDetectorCompat;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       return mGestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
        mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        mMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringBuilder = new StringBuilder();
                mMessageTextView.setText(mStringBuilder.toString());
            }
        });
        mGestureDetectorCompat = new GestureDetectorCompat(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onSingleTapUp");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onLongPress");
                mMessageTextView.setText(mStringBuilder.toString());
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                mStringBuilder.insert(0, "\r\n onScroll");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mStringBuilder.insert(0, "\r\n onFling");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onShowPress");
                mMessageTextView.setText(mStringBuilder.toString());
            }

            @Override
            public boolean onDown(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onDown");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onDoubleTap");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onDoubleTapEvent");
                mMessageTextView.setText(mStringBuilder.toString());
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onSingleTapConfirmed");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }
        });
        /*mGestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onDown");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onShowPress");
                mMessageTextView.setText(mStringBuilder.toString());
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onSingleTapUp");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                mStringBuilder.insert(0, "\r\n onScroll");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                mStringBuilder.insert(0, "\r\n onLongPress");
                mMessageTextView.setText(mStringBuilder.toString());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mStringBuilder.insert(0, "\r\n onFling");
                mMessageTextView.setText(mStringBuilder.toString());
                return false;
            }
        });*/
    }
}
