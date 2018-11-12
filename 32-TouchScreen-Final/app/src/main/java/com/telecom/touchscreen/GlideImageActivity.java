package com.telecom.touchscreen;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class GlideImageActivity extends AppCompatActivity {
    TextView mMsgTextView;
    ImageView mGirlImageView;
    GestureDetectorCompat mGestureDetectorCompat;
    int index = 0;
    private int[] imageDrawableIds = new int[]{
            R.drawable.girl_chuckle_icon,
            R.drawable.girl_confused_icon,
            R.drawable.girl_idea_icon,
            R.drawable.girl_in_love_icon,
            R.drawable.girl_motivated_icon,
            R.drawable.girl_swear_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_image);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mGirlImageView = (ImageView) findViewById(R.id.girl_image_view);
        mGestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX > 0)
                    index++;
                else
                    index--;
                if (index < 0)
                    index = 0;
                if (index >= imageDrawableIds.length)
                    index = imageDrawableIds.length - 1;
                mGirlImageView.setImageResource(imageDrawableIds[index]);
                mMsgTextView.setText(""+index);
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetectorCompat.onTouchEvent(event);
    }
}
