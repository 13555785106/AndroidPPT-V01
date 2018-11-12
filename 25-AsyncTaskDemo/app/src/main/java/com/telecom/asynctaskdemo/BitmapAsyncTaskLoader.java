package com.telecom.asynctaskdemo;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by xiaojf on 18/8/22.
 */

public class BitmapAsyncTaskLoader extends AsyncTaskLoader<Bitmap> {
    private final static String TAG = BitmapAsyncTaskLoader.class.getSimpleName();

    public BitmapAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public Bitmap loadInBackground() {
        Log.e(TAG, "loadInBackground");
        try {
            Thread.sleep(1000 * 8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.stellarium_128);
    }

    @Override
    protected void onStartLoading() {
        Log.e(TAG, "onStartLoading");
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // 取消加载
        cancelLoad();
    }

    @Override
    public void onCanceled(Bitmap data) {
        Log.e(TAG, "onCanceled(Bitmap data)");
        super.onCanceled(data);
    }

    @Override
    protected boolean onCancelLoad() {
        Log.e(TAG, "onCancelLoad()");
        return super.onCancelLoad();
    }

    @Override
    public void cancelLoadInBackground() {
        Log.e(TAG, "cancelLoadInBackground");
    }
}
