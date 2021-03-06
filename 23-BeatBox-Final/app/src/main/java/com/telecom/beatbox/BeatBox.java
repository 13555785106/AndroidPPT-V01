package com.telecom.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/1/3.
 */

public class BeatBox {
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final String IMAGES_FOLDER = "sample_images";
    private static final int MAX_SOUNDS = 5;
    private Context mContext;
    private SoundPool mSoundPool;
    private float mSoundRate = 1.0f;
    private List<SoundImage> mSoundImages = new ArrayList<>();

    public BeatBox(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        try {
            loadAssets();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loadAssets() throws IOException {
        for (String filename : mContext.getAssets().list(SOUNDS_FOLDER)) {
            String name = filename.replace(".wav", "");
            SoundImage soundImage = new SoundImage(SOUNDS_FOLDER, IMAGES_FOLDER, name);
            load(soundImage);
            mSoundImages.add(soundImage);
        }

    }

    public List<SoundImage> getSoundImages() {
        return mSoundImages;
    }

    public Drawable getImage(String name) {
        Drawable drawable = null;
        try {
            //System.out.println(mContext.getResources().getDisplayMetrics().density);
            //System.out.println(mContext.getResources().getDisplayMetrics().densityDpi);
            Bitmap oldBitmap = BitmapFactory.decodeStream(mContext.getAssets().openFd(IMAGES_FOLDER + "/" + name + ".png").createInputStream());
            //System.out.println(oldBitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.postScale(1, 1);
            Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
            drawable = new BitmapDrawable(mContext.getResources(), newBitmap);
            //drawable = new BitmapDrawable(mContext.getResources(), mAssets.openFd(IMAGES_FOLDER + "/" + name + ".png").createInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return drawable;
    }

    private void load(SoundImage soundImage) throws IOException {
        AssetFileDescriptor afd = mContext.getAssets().openFd(soundImage.getSoundPath() + "/" + soundImage.getName() + ".wav");
        int soundId = mSoundPool.load(afd, 1);
        soundImage.setSoundId(soundId);
    }

    public void play(SoundImage soundImage) {
        Integer soundId = soundImage.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, mSoundRate);
    }

    public float getSoundRate() {
        return mSoundRate;
    }

    public void setSoundRate(float soundRate) {
        mSoundRate = soundRate;
    }

    public void release() {
        mSoundPool.release();
    }
}
