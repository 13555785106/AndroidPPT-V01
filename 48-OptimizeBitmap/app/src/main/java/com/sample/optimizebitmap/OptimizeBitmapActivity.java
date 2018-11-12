package com.sample.optimizebitmap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OptimizeBitmapActivity extends AppCompatActivity {
    private final static String TAG = OptimizeBitmapActivity.class.getSimpleName();
    ImageView mImageView;
    Button mButtonZoomIn;
    Button mButtonZoomOut;
    TextView mTextViewMsg;
    ActivityManager mActivityManager;
    int mInSampleSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        setContentView(R.layout.activity_optimize_bitmap);
        mImageView = findViewById(R.id.image_view);
        loadPicture();
        View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_zoom_in:
                        mInSampleSize /= 2;
                        if (mInSampleSize < 1)
                            mInSampleSize = 1;
                        break;
                    case R.id.button_zoom_out:
                        mInSampleSize *= 2;
                        break;
                }

                loadPicture();
            }
        };
        findViewById(R.id.button_zoom_in).setOnClickListener(buttonOnClickListener);
        findViewById(R.id.button_zoom_out).setOnClickListener(buttonOnClickListener);
        mTextViewMsg = findViewById(R.id.text_view_msg);
        showMemInfo();
    }

    private void showMemInfo() {
        mTextViewMsg.append("getMemoryClass()=" + mActivityManager.getMemoryClass() + "M\r\n");
        mTextViewMsg.append("getLargeMemoryClass()=" + mActivityManager.getLargeMemoryClass() + "M\r\n");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        mTextViewMsg.append("memoryInfo.availMem=" + memoryInfo.availMem / 1024 / 1024 + "M\r\n");
        mTextViewMsg.append("memoryInfo.totalMem=" + memoryInfo.totalMem / 1024 / 1024 + "M\r\n");
        mTextViewMsg.append("memoryInfo.threshold=" + memoryInfo.threshold / 1024 / 1024 + "M\r\n");
        mTextViewMsg.append("memoryInfo.lowMemory=" + memoryInfo.lowMemory + "\r\n");
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(runningAppProcessInfo);
        mTextViewMsg.append("runningAppProcessInfo.lastTrimLevel=" + runningAppProcessInfo.lastTrimLevel + "\r\n");
        mTextViewMsg.append("runningAppProcessInfo.importance=" + runningAppProcessInfo.importance + "\r\n");
    }

    private void loadPicture() {

        Log.e(TAG,"densityDpi="+getResources().getDisplayMetrics().densityDpi);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;
        int jpgWidth = 0, jpgHeight = 0;

        BitmapFactory.decodeResource(getResources(), R.drawable.horse, bitmapFactoryOptions);

        jpgWidth = bitmapFactoryOptions.outWidth;
        jpgHeight = bitmapFactoryOptions.outHeight;
        Log.e(TAG, bitmapFactoryOptions.outWidth + " X " + bitmapFactoryOptions.outHeight);

        Bitmap bitmap = null;

        bitmapFactoryOptions.inSampleSize = mInSampleSize;

        if (jpgWidth > 0 && jpgHeight > 0) {

            bitmapFactoryOptions.inScaled = true;
            bitmapFactoryOptions.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
            bitmapFactoryOptions.inDensity = bitmapFactoryOptions.inTargetDensity/2;

            bitmapFactoryOptions.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.horse, bitmapFactoryOptions);
            mImageView.setImageBitmap(bitmap);

        }

    }
    /*
    private void loadPicture() {
        AssetManager assetManager = getResources().getAssets();

        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;
        int jpgWidth = 0, jpgHeight = 0;
        InputStream inputStream = null;
        Rect outPadding = new Rect(0, 0, 0, 0);
        try {
            inputStream = assetManager.open("horse.jpg");
            BitmapFactory.decodeStream(inputStream, outPadding, bitmapFactoryOptions);
            jpgWidth = bitmapFactoryOptions.outWidth;
            jpgHeight = bitmapFactoryOptions.outHeight;
            Log.e(TAG, bitmapFactoryOptions.outWidth + " X " + bitmapFactoryOptions.outHeight);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        Bitmap bitmap = null;

        bitmapFactoryOptions.inSampleSize = mInSampleSize;

        if (jpgWidth > 0 && jpgHeight > 0) {
            try {
                //bitmapFactoryOptions.inScaled = true;
                //bitmapFactoryOptions.inDensity = jpgWidth;
                //bitmapFactoryOptions.inTargetDensity = jpgWidth*mInSampleSize;
                bitmapFactoryOptions.inJustDecodeBounds = false;
                inputStream = assetManager.open("horse.jpg");
                bitmap = BitmapFactory.decodeStream(inputStream, outPadding, bitmapFactoryOptions);
                mImageView.setImageBitmap(bitmap);
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
        }

    }*/
}
