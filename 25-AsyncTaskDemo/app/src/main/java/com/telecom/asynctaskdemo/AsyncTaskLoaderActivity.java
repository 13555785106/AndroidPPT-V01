package com.telecom.asynctaskdemo;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AsyncTaskLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private final static String TAG = AsyncTaskLoaderActivity.class.getSimpleName();
    LoaderManager mLoaderManager;
    ImageView mImageView;
    Button mButtonStart;
    Button mButtonStop;
    TextView mTextViewMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_loader);
        mLoaderManager = getLoaderManager();
        mImageView = (ImageView) findViewById(R.id.image_view);
        mTextViewMsg = (TextView) findViewById(R.id.text_view_msg);
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoaderManager.restartLoader(1, null, AsyncTaskLoaderActivity.this);
                mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.task_128));
                mButtonStart.setEnabled(false);
                mButtonStop.setEnabled(true);
            }
        });
        mButtonStop = (Button) findViewById(R.id.button_stop);
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoaderManager.getLoader(1).cancelLoad();
                //mLoaderManager.destroyLoader(1);
                mTextViewMsg.setText("");
                mButtonStart.setEnabled(true);
                mButtonStop.setEnabled(false);
            }
        });
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "onCreateLoader");
        mTextViewMsg.setText("加载中，请等待。");
        return new BitmapAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        Log.e(TAG, "onLoadFinished");
        mTextViewMsg.setText("图片加载完毕。");
        mImageView.setImageBitmap(data);
        mButtonStart.setEnabled(true);
        mButtonStop.setEnabled(false);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        Log.e(TAG, "onLoaderReset");
    }
}
