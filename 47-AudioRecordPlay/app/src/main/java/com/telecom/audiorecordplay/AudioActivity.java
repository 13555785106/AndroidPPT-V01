package com.telecom.audiorecordplay;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class AudioActivity extends SingleFragmentActivity {
    AudioFragment mAudioFragment;
    @Override
    protected Fragment createFragment() {
        mAudioFragment = AudioFragment.newInstance();
        return mAudioFragment;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mAudioFragment!=null)
            mAudioFragment.showMediaController();
        return super.onTouchEvent(event);

    }
}
