package com.telecom.stepcounterfinal;

import android.support.v4.app.Fragment;

public class StepCounterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return StepCounterFragment.newInstance();
    }
}
