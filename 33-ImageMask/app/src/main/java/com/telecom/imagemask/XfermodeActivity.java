package com.telecom.imagemask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class XfermodeActivity extends AppCompatActivity {
    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };
    ImageView mImageView;
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode);
        mGridLayout = findViewById(R.id.grid_layout);
        mImageView = findViewById(R.id.image_view);
        for (int i = 0; i < sLabels.length; i++) {
            Button button = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();  //设置位置
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            button.setText(sLabels[i]);
            mGridLayout.addView(button, params);
            final int index = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap imgsrc = BitmapFactory.decodeResource(getResources(), R.drawable.imgsrc);
                    Bitmap imgdst = BitmapFactory.decodeResource(getResources(), R.drawable.imgdst);
                    final Bitmap result = Bitmap.createBitmap(imgsrc.getWidth(), imgsrc.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.BLACK);

                    canvas.drawBitmap(imgdst, 0, 0, paint);
                    paint.setXfermode(sModes[index]);
                    canvas.drawBitmap(imgsrc, 0, 0, paint);
                    paint.setXfermode(null);

                    mImageView.setImageBitmap(result);
                }
            });
        }

    }
}
