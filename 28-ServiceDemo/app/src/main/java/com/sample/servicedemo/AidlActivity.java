package com.sample.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AidlActivity extends AppCompatActivity {
    private final static String TAG = AidlActivity.class.getSimpleName();
    Button mButtonBindRemoteAidl;
    Button mButtonUnbindRemoteAidl;
    Button mButtonRemoteAidlSend;
    boolean isBound;
    private IRemoteInterface mIRemoteInterface;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mIRemoteInterface = IRemoteInterface.Stub.asInterface(service);
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");
            mIRemoteInterface = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        mButtonBindRemoteAidl = findViewById(R.id.button_bind_remote_aidl);
        mButtonBindRemoteAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent("com.sample.servicedemo.AidlService");
                serviceIntent.setPackage("com.sample.servicedemo");
                IntentUtils.printIntentInfo(serviceIntent);
                boolean bindResult = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                Log.e(TAG, "" + bindResult);
                mButtonBindRemoteAidl.setEnabled(false);
                mButtonUnbindRemoteAidl.setEnabled(true);
                mButtonRemoteAidlSend.setEnabled(true);
            }
        });
        mButtonUnbindRemoteAidl = findViewById(R.id.button_unbind_remote_aidl);
        mButtonUnbindRemoteAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound)
                    unbindService(mServiceConnection);
                mButtonBindRemoteAidl.setEnabled(true);
                mButtonUnbindRemoteAidl.setEnabled(false);
                mButtonRemoteAidlSend.setEnabled(false);
            }
        });
        mButtonRemoteAidlSend = findViewById(R.id.button_remote_aidl_send);
        mButtonRemoteAidlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AidlRequest aidlRequest = new AidlRequest();
                    mIRemoteInterface.sendRequest(aidlRequest);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
