package com.telecom.drawoncanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;

public class Sample06Activity extends AppCompatActivity {

    private final static String TAG = Sample06Activity.class.getSimpleName();
    AutoFitTextureView mAutoFitTextureView;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

            Log.e(TAG,"onSurfaceTextureAvailable width="+width+",height="+height);
            Log.e(TAG,"mAutoFitTextureView.getMeasuredWidth()="+mAutoFitTextureView.getMeasuredWidth()+",mAutoFitTextureView.getMeasuredHeight()="+mAutoFitTextureView.getMeasuredHeight());
            Canvas canvas = mAutoFitTextureView.lockCanvas();

            Rect dst = new Rect(0,0,mAutoFitTextureView.getMeasuredWidth(),mAutoFitTextureView.getMeasuredHeight());
            Rect src  = new Rect(0,0, mBitmap.getWidth(), mBitmap.getHeight());
            Log.e(TAG,"mAutoFitTextureView.getAspectRatio()="+mAutoFitTextureView.getAspectRatio());
            if(mAutoFitTextureView.getHeight()>mAutoFitTextureView.getWidth()) {
                canvas.translate(0,(mAutoFitTextureView.getHeight()-mAutoFitTextureView.getMeasuredHeight())/2);
            }else{
                canvas.translate((mAutoFitTextureView.getWidth()-mAutoFitTextureView.getMeasuredWidth())/2,0);
            }
            canvas.drawBitmap(mBitmap,src,dst,null);
            canvas.drawRect(dst,mRedPaint);


            mAutoFitTextureView.unlockCanvasAndPost(canvas);

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
    Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample06);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.girl_red_dress);
        mAutoFitTextureView = (AutoFitTextureView) findViewById(R.id.texture_view);
        mAutoFitTextureView.setAspectRatio(mBitmap.getWidth(),mBitmap.getHeight());
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
        if (mAutoFitTextureView.isAvailable()) {

        } else {
            mAutoFitTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
}
