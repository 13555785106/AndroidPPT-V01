package com.telecom.photogallery;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Created by xiaojf on 18/1/15.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PollJobService extends JobService {
    private final static String TAG = "PollJobService";
    private final static int POLL_JOB_SERVICE_ID = 1;

    public static void startService(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(POLL_JOB_SERVICE_ID, new ComponentName(context, PollJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(1000 * 60)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    public static void stopService(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(POLL_JOB_SERVICE_ID);
    }

    public static boolean isServiceAlarmOn(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == POLL_JOB_SERVICE_ID)
                return true;
        }
        return false;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        if(isOn){
            startService(context);
        }else {
            stopService(context);
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob " + params);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                PollService.pollingImage(PollJobService.this);
            }
        });
        t.start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob " + params);
        return false;
    }
}
