package com.telecom.ringandvibrate;

import android.support.v4.app.Fragment;

public class RingAndVibrateActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return RingAndVibrateFragment.newInstance();
    }
}
