package com.telecom.sms;

import android.support.v4.app.Fragment;

public class SMSActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Fragment fragment = SMSFragment.newInstance();
        return fragment;
    }
}
