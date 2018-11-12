package com.telecom.oneortwodimbarcode;

import android.support.v4.app.Fragment;

public class BarCodeActivity extends SingleFragmentActivity {

    BarCodeFragment mOneDimBarCodeFragment;

    @Override
    protected Fragment createFragment() {
        mOneDimBarCodeFragment = BarCodeFragment.newInstance();
        return mOneDimBarCodeFragment;
    }
}
