package com.telecom.suspendandwakeup;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    MainFragment mMainFragment;

    @Override
    protected Fragment createFragment() {
        mMainFragment = MainFragment.newInstance();
        return mMainFragment;
    }
}
