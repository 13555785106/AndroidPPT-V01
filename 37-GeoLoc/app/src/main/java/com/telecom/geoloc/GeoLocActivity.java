package com.telecom.geoloc;

import android.support.v4.app.Fragment;

public class GeoLocActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return GeoLocFragment.newInstance();
    }
}
