package com.telecom.suspendandwakeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView mTextViewMsg;
    Button mButtonStartDomainThread;
    BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mTextViewMsg.append("开屏\r\n");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mTextViewMsg.append("锁屏\r\n");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mTextViewMsg.append("解锁\r\n");
            }
        }
    };

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getActivity().registerReceiver(mScreenReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mTextViewMsg = v.findViewById(R.id.text_view_msg);
        mButtonStartDomainThread = v.findViewById(R.id.button_start_domain_thread);
        mButtonStartDomainThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("唤醒！！！！！！");


                        PowerManager mPowerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                        if (!mPowerManager.isScreenOn()) {
                            PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                            mWakeLock.acquire(); // 点亮屏幕
                            Notification notification = new NotificationCompat.Builder(getActivity(),"default")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentText("通知内容")
                                    .setContentTitle("通知标题")
                                    .setWhen(System.currentTimeMillis())
                                    .build();
                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, notification);
                            if (mWakeLock != null)
                                mWakeLock.release();
                        }

                    }
                });
                t.start();
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mScreenReceiver);
    }
}
