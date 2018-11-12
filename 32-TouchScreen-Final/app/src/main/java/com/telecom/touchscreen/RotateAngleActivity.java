package com.telecom.touchscreen;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RotateAngleActivity extends AppCompatActivity {
    Button mResetButton;
    TextView mMsgTextView;
    ImageView mGirlImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_angle);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGirlImageView.setRotation(0);
                mMsgTextView.setText("0");
            }
        });
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mGirlImageView = (ImageView) findViewById(R.id.girl_image_view);
        mGirlImageView.setOnTouchListener(new View.OnTouchListener() {
            int startPointId, endPointId;
            PointF preStartPoint, preEndPoint;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 2) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_POINTER_DOWN:
                            startPointId = event.getPointerId(0);
                            preStartPoint = new PointF(event.getX(0), event.getY(0));
                            endPointId = event.getPointerId(1);
                            preEndPoint = new PointF(event.getX(1), event.getY(1));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int startPointIndex = event.findPointerIndex(startPointId);
                            PointF startPoint = new PointF(event.getX(startPointIndex), event.getY(startPointIndex));
                            int endPointIndex = event.findPointerIndex(endPointId);
                            PointF endPoint = new PointF(event.getX(endPointIndex), event.getY(endPointIndex));
                            double deltaAngle = computeAngle(startPoint, endPoint)
                                    - computeAngle(preStartPoint, preEndPoint);
                            System.out.println(deltaAngle);
                            mGirlImageView.setRotation(mGirlImageView.getRotation() - (float) deltaAngle);
                            mMsgTextView.setText(Float.toString(mGirlImageView.getRotation()));
                            preStartPoint = startPoint;
                            preEndPoint = endPoint;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                    }
                }
                return true;
            }
        });
    }

    private double computeAngle(PointF start, PointF end) {
        return computeRadian(start, end) * 180 / Math.PI;
    }

    private double computeRadian(PointF start, PointF end) {
        double result = 0;
        float xDelta = end.x - start.x;
        float yDelta = end.y - start.y;
        if (xDelta != 0) {
            result = Math.abs(Math.atan(yDelta / xDelta));
            if (xDelta > 0) {
                if (yDelta < 0) {//第一象限

                }
                if (yDelta > 0) //第四象限
                    result = 2 * Math.PI - result;
            }
            if (xDelta < 0) {
                if (yDelta == 0)
                    result = Math.PI + result;
                if (yDelta < 0) //第二象限
                    result = Math.PI - result;

                if (yDelta > 0) //第三象限
                    result = Math.PI + result;
            }
        } else {
            if (yDelta < 0)
                result = 0.5 * Math.PI;
            if (yDelta > 0)
                result = 1.5 * Math.PI;
        }

        return result;
    }
}
