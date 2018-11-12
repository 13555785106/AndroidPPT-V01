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

public class Sample03Activity extends AppCompatActivity {

    private final static String TAG = Sample03Activity.class.getSimpleName();
    TextureView mTextureView;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Canvas canvas = mTextureView.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.scale(0.3f,0.3f);
            canvas.skew(0.1f, 0.1f);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.girl_red_dress);
            canvas.drawBitmap(bitmap,0,0,null);
            canvas.drawRect(0,0,bitmap.getWidth(),bitmap.getHeight(),mRedPaint);
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
    Paint mRedPaint = new Paint();
    Paint mGreenPaint = new Paint();
    Paint mBluePaint = new Paint();

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
