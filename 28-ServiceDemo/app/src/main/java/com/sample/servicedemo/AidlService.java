package com.sample.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

public class AidlService extends Service {
    private final static String TAG = AidlService.class.getSimpleName();
    private final IRemoteInterface.Stub binder = new IRemoteInterface.Stub() {
        @Override
        public boolean sendRequest(AidlRequest result) throws RemoteException {
            //此处不应进行长时间的操作，应该使用一个工作线程来处理，参照DemoService
            Log.e(TAG,result.toString());
            return false;
        }
    };
    @Override
    public void onCreate() {
        Log.e(TAG, "Pid:" + Process.myPid() + ",Tid:" + Process.myTid() + ",Uid:" + Process.myUid());
        Log.e(TAG, "onCreate");
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return binder;
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
    }
}
