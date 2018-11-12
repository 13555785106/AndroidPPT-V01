package com.telecom.touchscreen;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TouchScreenActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TouchScreenFragment.newInstance();
    }
}
