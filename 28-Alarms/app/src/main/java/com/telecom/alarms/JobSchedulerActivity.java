package com.telecom.alarms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import static android.app.job.JobScheduler.RESULT_SUCCESS;

public class JobSchedulerActivity extends Activity implements View.OnClickListener {

    //为每一个Job提供一个id
    private static final int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_main);

        //为两个按钮设置点击事件
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        long interval = 5 * 1000; //5 seconds
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("name", "John");
        bundle.putInt("age", 28);
        JobInfo info = new JobInfo.Builder(JOB_ID,
                new ComponentName(getPackageName(), WorkerService.class.getName()))
                .setPeriodic(interval).setExtras(bundle)
                .build();
        switch (view.getId()) {
            case R.id.start:
                int result = scheduler.schedule(info);
                if (result != RESULT_SUCCESS) {
                    Toast.makeText(this, "Error Scheduling Job", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stop:
                //指定Job ID 来停止
                scheduler.cancel(JOB_ID);
                break;
            default:
                break;
        }
    }
}
