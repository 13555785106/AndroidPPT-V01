package com.telecom.alarms;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class WorkerService extends JobService {

    private static final int MSG_JOB = 1;

    //Simple queue handler for executing the jobs that are scheduled
    private Handler mJobProcessor = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JobParameters params = (JobParameters) msg.obj;
            Log.i("WorkerService", "Executing Job " + params.getJobId());
            // 在完成我们的异步任务之后，必须触发jobFinished()允许下一个任务执行。
            doWork();
            jobFinished(params, false);
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("WorkerService", "Start Job " + jobParameters.getJobId());
        //这里模仿一个长任务，延迟7.5秒再运行
        mJobProcessor.sendMessageDelayed(
                Message.obtain(mJobProcessor, MSG_JOB, jobParameters),
                7500
        );

        /*
         * 如果任务已经同步完成，返回false,
         * 如果你有其它的事情要做，返回true.此时你要自己主动调用jobFinished()通知系统job已经完成。
         */
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.w("WorkerService", "Stop Job " + jobParameters.getJobId());
        //如果请求停止任务，我们必须取消未执行的job
        mJobProcessor.removeMessages(MSG_JOB);
        /*
         * 返回true表示已有的job继续会执行，false全部丢弃。
         */
        return false;
    }

    private void doWork() {
        //仅显示当前的时间
        Calendar now = Calendar.getInstance();
        DateFormat formatter = SimpleDateFormat.getTimeInstance();
        Toast.makeText(this, formatter.format(now.getTime()), Toast.LENGTH_SHORT).show();
    }
}
