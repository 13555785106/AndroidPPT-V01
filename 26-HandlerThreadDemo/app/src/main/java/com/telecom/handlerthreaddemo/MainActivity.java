package com.telecom.handlerthreaddemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG= MainActivity.class.getSimpleName();
    HandlerThread mCookHandlerThread;
    Handler mCookHandler;
    Handler mMainHandler;
    Button mButtonRun;
    ProgressBar mProgressBar;
    RadioGroup mRadioGroupDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonRun = findViewById(R.id.button_run);
        mProgressBar = findViewById(R.id.progress_bar);
        mRadioGroupDuration = findViewById(R.id.radio_group_duration);
        mCookHandlerThread = new HandlerThread("cook");
        mCookHandlerThread.start();
        mCookHandler = new Handler(mCookHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Message message = Message.obtain();
                    message.what = 1;
                    mMainHandler.sendMessage(message);
                    int duration = msg.getData().getInt("duration", 0);
                    int step = 500;
                    for (int i = 0; i < duration * 1000; i += step) {
                        try {
                            Thread.sleep(step);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        message = Message.obtain();
                        message.what = 0;
                        message.getData().putInt("cur", i+step);
                        message.getData().putInt("max", duration * 1000);
                        mMainHandler.sendMessage(message);
                    }
                    message = Message.obtain();
                    message.what = 2;
                    mMainHandler.sendMessage(message);
                }
            }
        };
        mMainHandler = new Handler(/*getMainLooper()*/) {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG,""+msg.what);
                switch (msg.what) {
                    case 0:
                        int cur = msg.getData().getInt("cur");
                        int max = msg.getData().getInt("max");
                        mProgressBar.setProgress((int)(cur*1.0/max*100));
                        break;
                    case 1:
                        mButtonRun.setVisibility(View.GONE);
                        break;
                    case 2:
                        mButtonRun.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        mButtonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = 0;
                switch (mRadioGroupDuration.getCheckedRadioButtonId()) {
                    case R.id.radio_button_second10:
                        duration = 10;
                        break;
                    case R.id.radio_button_second20:
                        duration = 20;
                        break;
                    case R.id.radio_button_second30:
                        duration = 30;
                        break;
                    case R.id.radio_button_second40:
                        duration = 40;
                        break;
                    case R.id.radio_button_second50:
                        duration = 50;
                        break;
                }
                Message msg = Message.obtain();
                msg.what = 0;
                msg.getData().putInt("duration", duration);
                mCookHandler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCookHandlerThread != null)
            mCookHandlerThread.quitSafely();
    }
}
