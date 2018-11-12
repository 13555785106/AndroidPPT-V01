package com.sample.callremoteservice;

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
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    Messenger messenger = null;// 存储服务器端的Messenger
    boolean isBound;
    Button mButtonBindRemoteService;
    Button mButtonUnbindRemoteService;
    Button mButtonRemoteServiceSend;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            messenger = new Messenger(service);
            isBound = true;
        }

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
        mButtonBindRemoteService = findViewById(R.id.button_bind_remote_service);
        mButtonBindRemoteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent("com.sample.servicedemo.DemoService");
                serviceIntent.setPackage("com.sample.servicedemo");
                IntentUtils.printIntentInfo(serviceIntent);
                boolean bindResult = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                Log.e(TAG, "" + bindResult);
                mButtonBindRemoteService.setEnabled(false);
                mButtonUnbindRemoteService.setEnabled(true);
                mButtonRemoteServiceSend.setEnabled(true);
            }
        });
        mButtonUnbindRemoteService = findViewById(R.id.button_unbind_remote_service);
        mButtonUnbindRemoteService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound)
                    unbindService(mServiceConnection);
                mButtonBindRemoteService.setEnabled(true);
                mButtonUnbindRemoteService.setEnabled(false);
                mButtonRemoteServiceSend.setEnabled(false);
            }
        });
        mButtonRemoteServiceSend = findViewById(R.id.button_remote_service_send);
        mButtonRemoteServiceSend.setOnClickListener(new View.OnClickListener() {
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

}
