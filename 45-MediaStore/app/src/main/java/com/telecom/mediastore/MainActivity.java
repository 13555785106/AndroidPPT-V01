package com.telecom.mediastore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;

public class MainActivity extends SingleFragmentActivity{
    MainFragment mMainFragment;
    @Override
    protected Fragment createFragment() {
        mMainFragment = MainFragment.newInstance();
        return mMainFragment;
    }
}
