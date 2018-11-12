package com.telecom.drawoncanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;

public class Sample01Activity extends AppCompatActivity {
    private final static String TAG = Sample01Activity.class.getSimpleName();
    TextureView mTextureView;
    Paint mRedPaint = new Paint();
    Paint mGreenPaint = new Paint();
    Paint mBluePaint = new Paint();
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Canvas canvas = mTextureView.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawCircle(width / 2, height / 2, width > height ? height / 2 : width / 2, mRedPaint);
            canvas.drawArc(0, 0, width, height, 0, 90, false, mRedPaint);
            canvas.drawArc(0, 0, width, height, 90, 90, false, mGreenPaint);
            canvas.drawArc(0, 0, width, height, 180, 90, true, mBluePaint);
            canvas.drawRect(0, 0, width, height, mRedPaint);
            canvas.drawLine(0, 0, width, height / 2, mRedPaint);

            float[] points = new float[8];
            for (int i = 0; i < points.length; i += 2) {
                points[i] = (float) (width * Math.random());
                points[i + 1] = (float) (height * Math.random());
            }
            for (int i = 0; i < points.length - 2; i += 2) {
                canvas.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3], mRedPaint);
            }
            for (int w = 0, h = 0; w < width && h < height; w += width / 80, h += height / 80) {
                canvas.drawPoint(w, h, mRedPaint);
            }
            mGreenPaint.setTextSize(72);
            canvas.drawText("大兄弟,听说中国厉害了,真的吗？", 0, height / 4, mGreenPaint);
            mGreenPaint.setTextSize(96);
            canvas.drawText("名不虚传的厉害国,^_^！", 0, height / 2, mGreenPaint);
            canvas.save();
            canvas.clipRect(width / 2, height / 2, width, height);
            canvas.drawColor(Color.RED);
            canvas.drawLine(0, 0, width, height, mBluePaint);
            canvas.restore();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly);
            canvas.drawBitmap(bitmap, 0, 0, null);
            mTextureView.unlockCanvasAndPost(canvas);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            Log.e(TAG, "onSurfaceTextureSizeChanged width=" + width + ",height=" + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample01);
        mTextureView = (TextureView) findViewById(R.id.texture_view);

        mRedPaint.setColor(Color.RED);
        mRedPaint.setStyle(Paint.Style.STROKE);
        mRedPaint.setStrokeCap(Paint.Cap.ROUND);
        mRedPaint.setStrokeWidth(8);

        mGreenPaint.setColor(Color.GREEN);
        mGreenPaint.setStyle(Paint.Style.FILL);
        mGreenPaint.setStrokeCap(Paint.Cap.ROUND);
        mGreenPaint.setStrokeWidth(8);

        mBluePaint.setColor(Color.BLUE);
        mBluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBluePaint.setStrokeCap(Paint.Cap.ROUND);
        mBluePaint.setStrokeWidth(8);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
}
