package com.telecom.phonecall;

import android.support.v4.app.Fragment;

public class PhoneCallActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment fragment = PhoneCallFragment.newInstance();
        return fragment;
    }
}
