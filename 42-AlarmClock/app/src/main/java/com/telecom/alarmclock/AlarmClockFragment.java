package com.telecom.alarmclock;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockFragment extends Fragment implements View.OnClickListener {
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    Button mAfter30sButton;
    Button mCancelAfter30sButton;
    Button mInterval30sButton;
    Button mCancelInterval30sButton;
    TextView mMsgTextView;
    AlarmManager mAlarmManager = null;

    public static AlarmClockFragment newInstance() {
        return new AlarmClockFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

        mMsgTextView = v.findViewById(R.id.msg_text_view);

        mAfter30sButton = v.findViewById(R.id.after_30s_button);
        mAfter30sButton.setOnClickListener(this);

        mCancelAfter30sButton = v.findViewById(R.id.cancel_after_30s_button);
        mCancelAfter30sButton.setOnClickListener(this);

        mInterval30sButton = v.findViewById(R.id.interval_30s_button);
        mInterval30sButton.setOnClickListener(this);

        mCancelInterval30sButton = v.findViewById(R.id.cancel_interval_30s_button);
        mCancelInterval30sButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {//相同的Intent信息与相同的请求码，会被认作同一个PendingIntent !
        Intent intent = new Intent(getActivity(), getActivity().getClass());
        if (v.getId() == R.id.after_30s_button) {
            intent.putExtra("message", getString(R.string.after_30s_button_text) + " 发:" + mSimpleDateFormat.format(new Date()));
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getActivity(),
                    1,
                    intent,
                    0);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, new Date().getTime(), pendingIntent);
        }else if(v.getId() == R.id.cancel_after_30s_button) {
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getActivity(),
                    1,
                    intent,
                    0);
            mAlarmManager.cancel(pendingIntent);
        }else if (v.getId() == R.id.interval_30s_button) {
            intent.putExtra("message", getString(R.string.interval_30s_button_text) + " 发:" + mSimpleDateFormat.format(new Date()));
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getActivity(),
                    2,
                    intent,
                    0);
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), 30 * 1000, pendingIntent);
        }else if(v.getId() == R.id.cancel_interval_30s_button){
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getActivity(),
                    2,
                    intent,
                    0);
            mAlarmManager.cancel(pendingIntent);
        }
        System.out.println("/*------------------------------------------------------------------------*/");
    }

    public void onNewIntent(Intent intent) {
        IntentUtils.printIntentInfo(intent);
        String msg = intent.getStringExtra("message");
        mMsgTextView.setText(msg + " 收：" + mSimpleDateFormat.format(new Date()) + "\r\n" + mMsgTextView.getText());
    }
}
