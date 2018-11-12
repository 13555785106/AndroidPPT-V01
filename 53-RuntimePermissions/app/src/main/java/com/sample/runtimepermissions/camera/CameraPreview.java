/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sample.runtimepermissions.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;

    public CameraPreview(Context context) {
        this(context, null, null, 0);
    }

    public CameraPreview(Context context, Camera camera, Camera.CameraInfo cameraInfo, int displayOrientation) {
        super(context);

        // 如果相机没有初始化，则不实例化.
        if (camera == null || cameraInfo == null) {
            return;
        }
        mCamera = camera;
        mCameraInfo = cameraInfo;
        mDisplayOrientation = displayOrientation;

        // 安装一个 SurfaceHolder.Callback，在surface创建和销毁时调用.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * 为预览计算正确的方向.
     */
    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Surface已经被创建,现在通知相机绘画预览.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            Log.d(TAG, "相机预览已经启动.");
        } catch (IOException e) {
            Log.d(TAG, "相机预览设置错误: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // 这里是空实现.在你的activity中小心处理释放相机预览有关的资源.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // 如果预览能改变或者旋转,仔细处理这个事件.
        // 在改变大小或重新格式化之前停止相机预览.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.d(TAG, "预览surface不存在");
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Log.d(TAG, "预览已停止.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "相机预览启动错误: " + e.getMessage());
        }

        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.d(TAG, "相机预览已启动.");
        } catch (Exception e) {
            Log.d(TAG, "相机预览时错误: " + e.getMessage());
        }
    }
}
