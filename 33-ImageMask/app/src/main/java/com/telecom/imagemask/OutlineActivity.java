package com.telecom.imagemask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OutlineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.CENTER);

        //抬高视图以建立阴影
        iv.setElevation(32f);

        iv.setImageResource(R.drawable.dog);

        //告诉视图使用轮廓线裁剪
        iv.setClipToOutline(true);
        //设置轮廓线来裁剪矩形
        iv.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                ImageView iv = (ImageView) view;
                int radius = iv.getDrawable().getIntrinsicHeight() / 2;
                int centerX = (view.getRight() - view.getLeft()) / 2;
                int centerY = (view.getBottom() - view.getTop()) / 2;

                outline.setOval(centerX - radius,
                        centerY - radius,
                        centerX + radius,
                        centerY + radius);
            }
        });

        setContentView(iv);
    }
}
