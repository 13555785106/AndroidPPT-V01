package com.telecom.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/1/3.
 */

public class BeatBox {
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final String IMAGES_FOLDER = "sample_images";
    private AssetManager mAssets;
    private Context mContext;
    private List<SoundImage> mSoundImages = new ArrayList<>();
    public BeatBox(Context context) {
        mContext = context;
        mAssets = context.getAssets();
        try {
            loadAssets();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void loadAssets() throws IOException {

        for (String filename : mAssets.list(SOUNDS_FOLDER)) {
            System.out.println(filename);
            String name = filename.replace(".wav", "");
            SoundImage soundImage = new SoundImage(SOUNDS_FOLDER,IMAGES_FOLDER,name);
            mSoundImages.add(soundImage);
            System.out.println(getImage(name));
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
            Bitmap oldBitmap = BitmapFactory.decodeStream(mAssets.openFd(IMAGES_FOLDER + "/" + name + ".png").createInputStream());
            //System.out.println(oldBitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.postScale(1, 1);
            Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
            drawable = new BitmapDrawable(mContext.getResources(),newBitmap);
            //drawable = new BitmapDrawable(mContext.getResources(), mAssets.openFd(IMAGES_FOLDER + "/" + name + ".png").createInputStream());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return drawable;
    }
}
