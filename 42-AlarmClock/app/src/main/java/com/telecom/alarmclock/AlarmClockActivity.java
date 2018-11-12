package com.telecom.alarmclock;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class AlarmClockActivity extends SingleFragmentActivity {
    AlarmClockFragment mAlarmClockFragment;

    @Override
    protected Fragment createFragment() {
        mAlarmClockFragment = AlarmClockFragment.newInstance();
        return mAlarmClockFragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mAlarmClockFragment != null)
            mAlarmClockFragment.onNewIntent(intent);
    }
}
