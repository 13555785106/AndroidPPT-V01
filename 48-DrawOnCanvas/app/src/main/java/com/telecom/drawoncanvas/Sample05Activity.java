package com.telecom.drawoncanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

public class Sample05Activity extends AppCompatActivity {

    private final static String TAG = Sample05Activity.class.getSimpleName();
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
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open(mPicNames[mIndex]);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF dst = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        canvas.setMatrix(matrix);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mMarkPaint);
            canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), mMarkPaint);
            if (++mIndex >= mPicNames.length)
                mIndex = 0;
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
