package com.telecom.baidumap;

import android.support.v4.app.Fragment;

public class BaiDuMapActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return BaiDuMapFragment.newInstance();
    }
}
