package com.telecom.imagemask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

public class MaskActivity extends Activity {
    private static final String TAG = MaskActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView iv = new ImageView(this);
        iv.setScaleType(ImageView.ScaleType.CENTER);

        //加载位图，此时的位图是只读不可修改的
        Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.triangle);

        //创建一个可修改的位图
        final Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        paint.setXfermode(null);

        iv.setImageBitmap(result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置阴影
            iv.setElevation(32f);
            //绘制轮廓线来遮盖位图
            iv.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int x = (view.getWidth() - result.getWidth()) / 2;
                    int y = (view.getHeight() - result.getHeight()) / 2;
                    Log.e(TAG, "x=" + x + ",y=" + y);
                    Path path = new Path();
                    path.moveTo(x, y);
                    path.lineTo(x + result.getWidth(), y);
                    path.lineTo(x + result.getWidth() / 2, y + result.getHeight());
                    path.lineTo(x, y);
                    path.close();

                    outline.setConvexPath(path);
                }
            });
        }
        setContentView(iv);
    }
}