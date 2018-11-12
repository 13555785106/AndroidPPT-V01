package com.telecom.asynctaskdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTaskActivity extends AppCompatActivity {
    Button mStartButton;
    Button mStopButton;
    TextView mPercentTextView;
    TextView mMsgTextView;
    ProgressBar mProgressBar;
    ImageView mAlarmImageView;
    Button mButtonAsyncTaskLoader;
    AsyncTask<Integer, Double, Integer> mSumAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        mAlarmImageView = (ImageView) findViewById(R.id.alarm_image_view);
        mPercentTextView = (TextView) findViewById(R.id.percent_text_view);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mButtonAsyncTaskLoader = (Button) findViewById(R.id.button_async_task_loader);
        mButtonAsyncTaskLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AsyncTaskActivity.this,AsyncTaskLoaderActivity.class));
            }
        });
        mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSumAsyncTask = new AsyncTask<Integer, Double, Integer>() {
                    @Override
                    protected Integer doInBackground(Integer... params) {
                        int sum = 0;
                        int start = params[0];
                        int end = params[1];
                        for (int i = start; i <= end; i++) {
                            sum += i;
                            double percent = (i - start + 1) * 1.0 / (end - start + 1);
                            Log.e("AsyncTask", "" + percent);
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            publishProgress(new Double[]{percent, sum * 1.0});
                        }
                        return sum;
                    }

                    @Override
                    protected void onProgressUpdate(Double... values) {
                        double percent = values[0];
                        int sum = values[1].intValue();
                        mProgressBar.setProgress((int) (percent * 100));
                        mPercentTextView.setText(Integer.toString(mProgressBar.getProgress()) + "%");
                        int num = mProgressBar.getProgress() % 10;
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < num; i++)
                            stringBuilder.append(".");
                        mMsgTextView.setText("求和计算进行中" + stringBuilder.toString() + " " + sum);
                    }

                    @Override
                    protected void onPreExecute() {
                        mStartButton.setEnabled(false);
                        mStopButton.setEnabled(true);
                        mMsgTextView.setText("求和计算开始");
                        mAlarmImageView.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onPostExecute(Integer sum) {
                        mStartButton.setEnabled(true);
                        mStopButton.setEnabled(false);
                        mProgressBar.setProgress(0);
                        mPercentTextView.setText("");
                        mMsgTextView.setText("求和计算结束，结果为 " + sum);
                    }

                    @Override
                    protected void onCancelled(Integer integer) {
                        mStartButton.setEnabled(true);
                        mStopButton.setEnabled(false);
                        mMsgTextView.setText("求和计算被取消!");

                    }
                };
                mSumAsyncTask.execute(new Integer[]{0, 100});
            }
        });
        mStopButton = (Button) findViewById(R.id.button_stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSumAsyncTask != null && mSumAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    mSumAsyncTask.cancel(false);
                    mMsgTextView.setText("正在取消求和计算，请等待!");
                    mStopButton.setEnabled(false);
                    mAlarmImageView.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
