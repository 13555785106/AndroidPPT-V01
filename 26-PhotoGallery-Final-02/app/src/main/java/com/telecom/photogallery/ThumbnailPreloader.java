package com.telecom.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * Created by xiaojf on 18/1/10.
 */

public class ThumbnailPreloader extends HandlerThread {
    private static final String TAG = "ThumbnailPreloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    private Handler mRequestHandler;
    private LruCache<String, Bitmap> mLruCache;

    public ThumbnailPreloader(LruCache<String, Bitmap> lruCache) {
        super(TAG);
        mLruCache = lruCache;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    String url = (String) msg.obj;
                    Bitmap bitmap = mLruCache.get(url);
                    if (bitmap == null) {
                        try {
                            byte[] bitmapBytes = PhotoFetchr.getUrlBytes(url);
                            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                            mLruCache.put(url, bitmap);
                            Log.i(TAG, url + " 图像被预先加载");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    public void queueThumbnail(String url) {
        mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, url).sendToTarget();
    }
}
