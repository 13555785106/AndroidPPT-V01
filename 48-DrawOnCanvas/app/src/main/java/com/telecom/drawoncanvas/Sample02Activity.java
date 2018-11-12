package com.telecom.drawoncanvas;

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

public class Sample02Activity extends AppCompatActivity {

    private final static String TAG = Sample02Activity.class.getSimpleName();
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
            canvas.drawLine(width / 2, height / 2, width * 3 / 4, height * 3 / 4, mRedPaint);
            canvas.drawRect(width * 3 / 4, height * 3 / 4, width, height, mBluePaint);
            canvas.drawArc(width * 3 / 4, height * 3 / 4, width, height, 0, 360, true, mGreenPaint);
            canvas.save();
            canvas.translate(0,-height / 2);
            canvas.drawLine(width / 2, height / 2, width * 3 / 4, height * 3 / 4, mRedPaint);
            canvas.drawRect(width * 3 / 4, height * 3 / 4, width, height, mBluePaint);
            canvas.drawArc(width * 3 / 4, height * 3 / 4, width, height, 0, 360, true, mGreenPaint);
            canvas.restore();
            canvas.save();
            canvas.translate(0,-height / 2);
            canvas.scale(-0.5f,0.5f,width/2,height/2);
            canvas.drawLine(width / 2, height / 2, width * 3 / 4, height * 3 / 4, mRedPaint);
            canvas.drawRect(width * 3 / 4, height * 3 / 4, width, height, mBluePaint);
            canvas.drawArc(width * 3 / 4, height * 3 / 4, width, height, 0, 360, true, mGreenPaint);
            canvas.restore();
            canvas.save();
            Matrix matrix = new Matrix();
            matrix.postRotate(180,width/2,height/2);
            matrix.postScale(0.5f,-0.5f,width/2,height/2);
            matrix.postTranslate(width/8,height/8);
            canvas.setMatrix(matrix);
            canvas.drawLine(width / 2, height / 2, width * 3 / 4, height * 3 / 4, mRedPaint);
            canvas.drawRect(width * 3 / 4, height * 3 / 4, width, height, mBluePaint);
            canvas.drawArc(width * 3 / 4, height * 3 / 4, width, height, 0, 360, true, mGreenPaint);
            canvas.restore();
            /*canvas.save();
            canvas.rotate(180,width/2,height/2);
            canvas.scale(0.5f,-0.5f,width/2,height/2);
            canvas.translate(width/8,height/8);
            canvas.drawLine(width / 2, height / 2, width * 3 / 4, height * 3 / 4, mRedPaint);
            canvas.drawRect(width * 3 / 4, height * 3 / 4, width, height, mBluePaint);
            canvas.drawArc(width * 3 / 4, height * 3 / 4, width, height, 0, 360, true, mGreenPaint);
            canvas.restore();*/

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
