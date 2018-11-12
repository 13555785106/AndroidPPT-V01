package com.telecom.touchscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

public class MotionEventActivity extends AppCompatActivity {
    TextView mTouchMeTextView;
    TextView mMessageTextView;
    StringBuilder mStringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_event);
        mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        mMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringBuilder = new StringBuilder();
                mMessageTextView.setText(mStringBuilder.toString());
            }
        });
        mTouchMeTextView = (TextView) findViewById(R.id.touch_me_text_view);
        mTouchMeTextView.setOnTouchListener(new View.OnTouchListener() {
            VelocityTracker mVelocityTracker = null;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mStringBuilder.insert(0, "\r\n");
                mStringBuilder.insert(0, ViewUtils.meSimpleString(event));
                mMessageTextView.setText(mStringBuilder.toString());
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                } else if (action == MotionEvent.ACTION_MOVE) {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    mStringBuilder.insert(0, "\r\n");
                    mStringBuilder.insert(0, "V(X=" + (int)mVelocityTracker.getXVelocity() + "px/s,Y=" + (int)mVelocityTracker.getYVelocity() + "px/s)");
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    mVelocityTracker = null;
                }
                return true;
            }
        });
    }
}
