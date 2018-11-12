package com.sample.optimizememory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;
/*
因为垃圾回收时间不可控，要想看此演示，必须自己触发垃圾回收。
 */
public class ActivityLeak2 extends AppCompatActivity {
    private static final String TAG = ActivityLeak2.class.getSimpleName();
    Button mStartButton;
    Button mStopButton;
    TextView mPercentTextView;
    TextView mMsgTextView;
    ProgressBar mProgressBar;
    ImageView mAlarmImageView;
    AsyncTask<Integer, Double, Integer> mSumAsyncTask;

    @Override
    protected void onDestroy() {
        Log.e(TAG,"ActivityLeak2 被销毁");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG){
            StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setVmPolicy(policy);
            System.out.println("启用了StrictMode!!!!!!!!");
        }



        setContentView(R.layout.activity_async_task);
        mAlarmImageView = (ImageView)findViewById(R.id.alarm_image_view);
        mPercentTextView = (TextView) findViewById(R.id.percent_text_view);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSumAsyncTask = new SumAsyncTask(ActivityLeak2.this);
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
    private static class SumAsyncTask extends AsyncTask<Integer, Double, Integer>{
        private WeakReference<ActivityLeak2> activityLeak2;
        public SumAsyncTask(ActivityLeak2 activityLeak2){
            this.activityLeak2 = new WeakReference<ActivityLeak2>(activityLeak2);
        }
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
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(activityLeak2.get());
                if(activityLeak2.get()!=null)
                    publishProgress(new Double[]{percent,sum*1.0});
                else
                    break;
            }
            return sum;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            double percent = values[0];
            int sum = values[1].intValue();
            if(activityLeak2.get()!=null) {
                activityLeak2.get().mProgressBar.setProgress((int) (percent * 100));
                activityLeak2.get().mPercentTextView.setText(Integer.toString(activityLeak2.get().mProgressBar.getProgress()) + "%");
                int num = activityLeak2.get().mProgressBar.getProgress() % 10;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < num; i++)
                    stringBuilder.append(".");
                activityLeak2.get().mMsgTextView.setText("求和计算进行中" + stringBuilder.toString() + " " + sum);
            }
        }

        @Override
        protected void onPreExecute() {
            if(activityLeak2.get()!=null) {
                activityLeak2.get().mStartButton.setEnabled(false);
                activityLeak2.get().mStopButton.setEnabled(true);
                activityLeak2.get().mMsgTextView.setText("求和计算开始");
                activityLeak2.get().mAlarmImageView.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Integer sum) {
            if(activityLeak2.get()!=null) {
                activityLeak2.get().mStartButton.setEnabled(true);
                activityLeak2.get().mStopButton.setEnabled(false);
                activityLeak2.get().mProgressBar.setProgress(0);
                activityLeak2.get().mPercentTextView.setText("");
                activityLeak2.get().mMsgTextView.setText("求和计算结束，结果为 " + sum);
            }
        }

        @Override
        protected void onCancelled(Integer integer) {
            if(activityLeak2.get()!=null) {
                activityLeak2.get().mStartButton.setEnabled(true);
                activityLeak2.get().mStopButton.setEnabled(false);
                activityLeak2.get().mMsgTextView.setText("求和计算被取消!");
            }

        }

    }
}
