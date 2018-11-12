package com.sample.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    Messenger messenger = null;// 存储服务器端的Messenger
    boolean isBound;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        /* 绑定服务成功时被调用
         */
        public void onServiceConnected(ComponentName className, IBinder service) {
            messenger = new Messenger(service);
            isBound = true;
        }
        // onServiceDisconnected()在连接正常关闭(unbindService)的情况下是不会被调用的,
        // 该方法只在Service 被破坏了或者被杀死的时候调用.例如, 系统资源不足,
        // 要关闭一些Services, 刚好连接绑定的 Service 是被关闭者之一,
        // 这个时候onServiceDisconnected() 就会被调用。
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");
            messenger = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "Pid:" + Process.myPid() + ",Tid:" + Process.myTid() + ",Uid:" + Process.myUid());
        Log.e(TAG, "onServiceConnected");
        Intent serviceIntent = new Intent("com.sample.servicedemo.DemoService");
        serviceIntent.setPackage("com.sample.servicedemo");
        IntentUtils.printIntentInfo(serviceIntent);
        boolean bindResult = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "" + bindResult);
        findViewById(R.id.button_local_service_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this,DemoService.class);
                startService(serviceIntent);
            }
        });
        findViewById(R.id.button_remote_service_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_aidl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AidlActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {

        if (isBound)
            unbindService(mServiceConnection);
        super.onDestroy();
    }
}
