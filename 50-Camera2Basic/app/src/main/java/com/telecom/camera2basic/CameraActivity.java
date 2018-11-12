package com.telecom.camera2basic;

import android.support.v4.app.Fragment;

public class CameraActivity extends SingleFragmentActivity {

    CameraFragment mCameraFragment;

    @Override
    protected Fragment createFragment() {
        mCameraFragment = CameraFragment.newInstance();
        return mCameraFragment;
    }
}
