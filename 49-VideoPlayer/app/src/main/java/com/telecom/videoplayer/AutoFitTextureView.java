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

package com.telecom.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * 可调整为指定的纵横比
 */
public class AutoFitTextureView extends TextureView {
    private static final String TAG = AutoFitTextureView.class.getSimpleName();
    private int mRatioWidth = 800;//宽度比例
    private int mRatioHeight = 600;//高度比例

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
     * 这里是设置的是比例，不是实际大小，setAspectRatio(2, 3) 与 setAspectRatio(4, 6)效果是相同的
     *
     * @param width  相对横向尺寸
     * @param height 相对纵向尺寸
     */
    public void setAspectRatio(int width, int height) {//设置横纵比
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("尺寸不能为负数.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    private String getMeasureSpecMode(int measureSpec){
        switch (MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.UNSPECIFIED:
                return "MeasureSpec.UNSPECIFIED";
            case MeasureSpec.EXACTLY:
                return "MeasureSpec.EXACTLY";
            case MeasureSpec.AT_MOST:
                return "MeasureSpec.AT_MOST";
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"widthMeasureSpec="+widthMeasureSpec);
        Log.e(TAG,"heightMeasureSpec="+heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(mRatioWidth, mRatioHeight);

        Log.e(TAG,"宽 spec="+getMeasureSpecMode(widthMeasureSpec));
        Log.e(TAG,"高 spec="+getMeasureSpecMode(heightMeasureSpec));
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.e(TAG,"宽 = "+width);
        Log.e(TAG,"高 = "+height);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                //如果给定的纵横比小于视图可用空间的纵横比
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                //如果给定的纵横比大于等于视图可用空间的纵横比
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

}
