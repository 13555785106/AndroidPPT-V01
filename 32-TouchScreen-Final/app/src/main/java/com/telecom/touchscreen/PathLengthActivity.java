package com.telecom.touchscreen;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PathLengthActivity extends AppCompatActivity {
    TextView mMsgTextView;
    ImageView mGirlImageView;
    List<PointF> mPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_length);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mGirlImageView = (ImageView) findViewById(R.id.girl_image_view);
        mGirlImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPoints.clear();
                        mPoints.add(new PointF(event.getX(), event.getY()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPoints.add(new PointF(event.getX(), event.getY()));
                        double length = computePathLength(mPoints);
                        String str = new DecimalFormat("0.00").format(length);
                        mMsgTextView.setText(str);
                        break;
                    case MotionEvent.ACTION_UP:
                        mPoints.clear();
                        break;
                }
                return true;
            }
        });
    }

    private double computePathLength(List<PointF> mPoints) {
        double length = 0;
        if (mPoints.size() >= 2) {
            for (int i = 0; i < mPoints.size() - 1; i++) {
                PointF start = mPoints.get(i);
                PointF end = mPoints.get(i+1);
                float deltaX = end.x - start.x;
                float deltaY = end.y = start.y;
                double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
                length += distance;
            }
        }
        return length;
    }
}
