package com.telecom.criminalintent;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by xiaojf on 17/12/24.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
