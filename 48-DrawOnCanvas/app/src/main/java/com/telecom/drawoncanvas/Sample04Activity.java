package com.telecom.drawoncanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

public class Sample04Activity extends AppCompatActivity {

    private final static String TAG = Sample04Activity.class.getSimpleName();
    TextureView mTextureView;
    Button mButtonNext;
    String[] mPicNames = new String[]{"pic00.jpg", "pic01.jpg", "pic02.jpg"};
    Paint mMarkPaint = new Paint();
    int mIndex = 0;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.e(TAG, "onSurfaceTextureAvailable width=" + width + ",height=" + height);

            loadPicture();
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

    private void loadPicture() {
        Canvas canvas = mTextureView.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Matrix matrix = new Matrix();
        float scale = 1;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            Log.e(TAG, "canvas width=" + canvas.getWidth() + ",height=" + canvas.getHeight());
            float canvasAspectRatio = canvas.getHeight() * 1.0f / canvas.getWidth();
            inputStream = getResources().getAssets().open(mPicNames[mIndex]);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.e(TAG, "mBitmap width=" + bitmap.getWidth() + ",height=" + bitmap.getHeight());
            float bitmapAspectRatio = bitmap.getHeight() * 1.0f / bitmap.getWidth();
            if (canvasAspectRatio >= bitmapAspectRatio) {
                scale = canvas.getWidth() * 1.0f / bitmap.getWidth();
                //从此处可以看出，此种变换都是以canvas原坐标系的进行的，而不是变换之后的
                matrix.postTranslate(0, (canvas.getHeight() - bitmap.getHeight() * scale) / 2 / scale);
            } else {
                scale = canvas.getHeight() * 1.0f / bitmap.getHeight();
                matrix.postTranslate((canvas.getWidth() - bitmap.getWidth() * scale) / 2 / scale, 0);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }finally {
            if(inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        Log.e(TAG, "scale=" + scale);
        matrix.postScale(scale, scale);
        matrix.postRotate(180);
        matrix.postScale(1, -1);
        matrix.postTranslate(canvas.getWidth(),0);
        canvas.setMatrix(matrix);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mMarkPaint);
            if (++mIndex >= mPicNames.length)
                mIndex = 0;
            canvas.drawRect(0, 0, bitmap.getWidth(),bitmap.getHeight(), mMarkPaint);
        }

        mTextureView.unlockCanvasAndPost(canvas);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample04);
        mMarkPaint.setColor(Color.RED);
        mMarkPaint.setStyle(Paint.Style.STROKE);
        mMarkPaint.setStrokeCap(Paint.Cap.ROUND);
        mMarkPaint.setStrokeWidth(8);
        mTextureView = (TextureView) findViewById(R.id.texture_view);
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPicture();
            }
        });
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
/*

if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            } else {//横屏
            }
 */