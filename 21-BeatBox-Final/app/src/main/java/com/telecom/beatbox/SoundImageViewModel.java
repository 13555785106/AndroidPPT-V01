package com.telecom.beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

/**
 * Created by xiaojf on 18/1/3.
 */

public class SoundImageViewModel extends BaseObservable {
    private SoundImage mSoundImage;
    private BeatBox mBeatBox;


    public SoundImageViewModel(BeatBox beatBox) {
        mBeatBox = beatBox;
    }

    @Bindable
    public String getTitle() {
        return mSoundImage.getName();
    }

    public SoundImage getSoundImage() {
        return mSoundImage;
    }

    public void setSoundImage(SoundImage soundImage) {
        mSoundImage = soundImage;
        notifyChange();
    }

    @Bindable
    public Drawable getImage() {
        return mBeatBox.getImage(mSoundImage.getName());
    }

    public void onButtonClicked() {
        mBeatBox.play(mSoundImage);
    }
}
