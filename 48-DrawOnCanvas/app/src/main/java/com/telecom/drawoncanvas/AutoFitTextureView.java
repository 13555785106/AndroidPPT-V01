/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.telecom.drawoncanvas;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * 能够调整纵横比的TextureView.
 */
public class AutoFitTextureView extends TextureView {
    private final static String TAG = AutoFitTextureView.class.getSimpleName();
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;


    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 为视图设置纵横比。这个参数设置的是纵横比，不是实际的宽度和高度。
     * 调用setAspectRatio(2, 3) 和 setAspectRatio(4, 6) 效果相同。
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("宽高系数不能是负数.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    public float getAspectRatio(){
        return mRatioHeight * 1f/mRatioWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"onMeasure");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.e(TAG,"onMeasure width="+width+",height="+width * mRatioHeight / mRatioWidth);
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                Log.e(TAG,"onMeasure width="+height * mRatioWidth / mRatioHeight+",height="+height);
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }

    }

}
