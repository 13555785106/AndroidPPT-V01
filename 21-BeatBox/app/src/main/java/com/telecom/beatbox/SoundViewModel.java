package com.telecom.beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by xiaojf on 18/1/3.
 */

public class SoundViewModel extends BaseObservable {
    private Sound mSound;
    private BeatBox mBeatBox;
    public SoundViewModel(BeatBox beatBox) {
        mBeatBox = beatBox;
    }
    public Sound getSound() {
        return mSound;
    }
    public void setSound(Sound sound) {
        mSound = sound;
        notifyChange();
    }
    @Bindable
    public String getTitle() {
        return mSound.getName();
    }

    public void onButtonClicked() {
        System.out.println("onButtonClicked" + mSound.getSoundId());
        mBeatBox.play(mSound);
    }
}
