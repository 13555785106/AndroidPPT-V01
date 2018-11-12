package com.telecom.beatbox;

/**
 * Created by xiaojf on 18/1/3.
 */

public class SoundImage {
    private String mSoundPath;
    private String mImagePath;
    private String mName;
    public SoundImage(String soundPath,String imagePath,String name) {
        mSoundPath = soundPath;
        mImagePath = imagePath;
        mName = name;
    }
    public String getName() {
        return mName;
    }

    public String getSoundPath() {
        return mSoundPath;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
