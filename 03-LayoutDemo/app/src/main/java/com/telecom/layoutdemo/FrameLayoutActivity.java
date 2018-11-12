package com.telecom.layoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FrameLayoutActivity extends AppCompatActivity {
    FrameLayout mFrameLayout;
    ImageView mAirplaneImageView;
    ImageView mBaloonImageView;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mAirplaneImageView = (ImageView) findViewById(R.id.airplane_image_view);
        mBaloonImageView = (ImageView) findViewById(R.id.baloon_image_view);
        mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0)
                    mAirplaneImageView.bringToFront();
                if (index == 1)
                    mBaloonImageView.bringToFront();
                index++;
                index %= 2;
            }
        });
    }
}
