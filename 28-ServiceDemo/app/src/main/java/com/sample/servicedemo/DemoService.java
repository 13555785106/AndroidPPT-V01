package com.sample.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;


public class DemoService extends Service {
    private final static String TAG = DemoService.class.getSimpleName();
    HandlerThread mHandlerThread;//为了避免ANR创建的工作线程，可长时间运行
    Handler mHandler;
    Messenger mMessenger;// 传递给客户端的Messenger

    @Override
    public void onCreate() {

        Log.e(TAG, "Pid:" + Process.myPid() + ",Tid:" + Process.myTid() + ",Uid:" + Process.myUid());
        Log.e(TAG, "onCreate");

        mHandlerThread = new HandlerThread("DemoServiceThread");//创建工作线程
        mHandlerThread.start();//启动工作线程
        //创建用于工作线程的Handler
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, msg.toString());
                Log.e(TAG, "Pid:" + Process.myPid() + ",Tid:" + Process.myTid() + ",Uid:" + Process.myUid());

            }
        };
        mMessenger = new Messenger(mHandler);//创建Messenger
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        mHandlerThread.quit();//注意，服务销毁退出工作线程
    }
    //
    /*
    *   onStartCommand返回值含义:
    *   1. START_STICKY
    *       返回该值，当中断发生后，Service会被重新创建，在重建的过程中，系统会发送一个值为null的intent。
    *       因此在使用Service.onStartCommand()方法中的intent之前，要判断其是否为null。
    *       如果你希望Service遇到一个意外而终止时能够重新启动，以继续完成工作，那么可以考虑该值。
    *   2. START_NOT_STICKY
    *       返回该值，当中断发生后，Service不会重新创建。直到调用一个正常的Service.onStartCommand()方法
    *       并传入一个新的intent,或者一个新的intent匹配到Service IntenFilter，才会重新启动它。
    *       所以这种情况下Service.onStartCommand()方法收到的intent不会是null.
    *       如果你希望Service遇到一个意外而终止，没必要重新启动继续完成工作，那么可以考虑该值。
    *   3. START_REDELIVER_INTENT
    *       当Service并非由于调用Service.stopSelf()或Service.stopService()方法而终止，那么当Service被重启时，
    *       Service.onStartCommand()方法会收到被销毁前所收到的最后一个intent。当我们需要知道什么操作导致了终端，可以考虑该值。
    *
    *    输入参数flags的含义:
    *    1. 0 默认值，表示intent首次正常传递进来。
    *    2. START_FLAG_REDELIVERY intent按返回策略被重新传递。这个intent之前被传递过一次，但是在任务完成之前，Service被意外终止了，
    *       因此intent被再次传递。
    *    3. START_FLAG_RETRY intent正要传递给Service时，Service被终止了。接着此intent被再次传递，并使用了该flag.
    *       根据该flag我们可以判断Service有没有处理过该intent。
    * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        Message message = Message.obtain();
        message.what = intent.getIntExtra("what", 0);
        message.setData(intent.getExtras());
        mHandler.sendMessage(message);
        return Service.START_NOT_STICKY;
    }

    //返回绑定式服务的Messenger的Binder
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mMessenger.getBinder();
    }

}
    /*
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind");
    }*/