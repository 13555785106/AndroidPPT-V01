package com.telecom.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HandlerDemoActivity extends AppCompatActivity {
    Button mStartButton;
    Button mStopButton;
    TextView mPercentTextView;
    TextView mMsgTextView;
    ProgressBar mProgressBar;
    ImageView mAlarmImageView;
    SumTask mSumTask;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.what);
            switch (msg.what) {
                case 0:
                    mMsgTextView.setText("求和计算开始");
                    mStartButton.setEnabled(false);
                    mStopButton.setEnabled(true);
                    mAlarmImageView.setVisibility(View.GONE);
                    break;
                case 1:
                    mProgressBar.setProgress(msg.getData().getInt("percent"));
                    mPercentTextView.setText(mProgressBar.getProgress() + "%");
                    int num = mProgressBar.getProgress() % 10;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < num; i++)
                        stringBuilder.append(".");
                    mMsgTextView.setText("求和计算进行中" + stringBuilder.toString() + " " + msg.getData().getInt("sum"));
                    break;
                case 2:
                    mStartButton.setEnabled(true);
                    mStopButton.setEnabled(false);
                    mProgressBar.setProgress(0);
                    mPercentTextView.setText("");
                    mMsgTextView.setText("求和计算结束，结果为 " + msg.getData().getInt("sum"));
                    break;
                case 3:
                    mStartButton.setEnabled(true);
                    mStopButton.setEnabled(false);
                    mMsgTextView.setText("求和计算被取消了！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_demo);
        mAlarmImageView = (ImageView) findViewById(R.id.alarm_image_view);
        mPercentTextView = (TextView) findViewById(R.id.percent_text_view);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSumTask = new SumTask(mHandler, 0, 100);
                Thread thread = new Thread(mSumTask);
                thread.start();
            }
        });
        mStopButton = (Button) findViewById(R.id.button_stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsgTextView.setText("正在取消求和计算，请等待!");
                mStopButton.setEnabled(false);
                mAlarmImageView.setVisibility(View.VISIBLE);
                mSumTask.setCanceled(true);
            }
        });
    }
}
