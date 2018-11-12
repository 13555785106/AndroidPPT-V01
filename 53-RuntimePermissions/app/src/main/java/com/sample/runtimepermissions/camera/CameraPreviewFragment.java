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

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sample.runtimepermissions.R;


public class CameraPreviewFragment extends Fragment {

    private static final String TAG = "CameraPreview";

    /**
     * 访问相机的Id. 0是第一个相机.
     */
    private static final int CAMERA_ID = 0;

    private CameraPreview mPreview;
    private Camera mCamera;

    public static CameraPreviewFragment newInstance() {
        return new CameraPreviewFragment();
    }

    /**
     * 获取相机对象的一个实例.
     */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // 尝试获取一个相机实例.
        } catch (Exception e) {
            // 相机不可用(使用中或者根本不存在).
            Log.d(TAG, "相机 " + cameraId + " 不可用: " + e.getMessage());
        }
        return c; // 如果相机不可用返回空值null.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // 打开第一个相机实例并获取信息.
        mCamera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;

        if (mCamera != null) {
            // 获取相机信息如果相机可用.
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        if (mCamera == null || cameraInfo == null) {
            // 如果相机不可用显示错误信息.
            Toast.makeText(getActivity(), "相机不可用.", Toast.LENGTH_SHORT).show();

            return inflater.inflate(R.layout.fragment_camera_unavailable, null);
        }

        View root = inflater.inflate(R.layout.fragment_camera, null);

        // 获取相机屏幕方向并调整预览图像.
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

        // 创建预览视图并设置其内容.
        mPreview = new CameraPreview(getActivity(), mCamera, cameraInfo, displayRotation);
        FrameLayout preview = (FrameLayout) root.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera access
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // 释放相机供其它程序使用.
            mCamera = null;
        }
    }
}
