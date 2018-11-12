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

package com.sample.runtimepermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sample.runtimepermissions.camera.CameraPreviewFragment;
import com.sample.runtimepermissions.contacts.ContactsFragment;


public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String TAG = "MainActivity";
    /**
     * 访问相机的请求码.
     */
    private static final int REQUEST_CAMERA = 0;

    /**
     * 访问联系人数据库的请求码.
     */
    private static final int REQUEST_CONTACTS = 1;

    /**
     * 用来读写联系人数据库的权限.
     */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

    /**
     * Activity布局的根.
     */
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sample_main_layout);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RuntimePermissionsFragment fragment = new RuntimePermissionsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    /**
     * 点击'显示相机'按钮被调用,回调定义在布局资源文件中.
     */
    public void showCamera(View view) {
        Log.i(TAG, "相机按钮被点击时显示,现在检查权限.");
        // BEGIN_INCLUDE(camera_permission)
        // 检查相机权限是否已经可用.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 相机权限还没有被授予.
            requestCameraPermission();
        } else {
            // 相机权限已经可用，显示相机预览.
            Log.i(TAG, "相机权限已经被授予，显示相机预览.");
            showCameraPreview();
        }
        // END_INCLUDE(camera_permission)
    }

    /**
     * 请求相机权限.如果权限以前被拒绝，一个SnackBar提示用户去授权，否则直接请求授权.
     */
    private void requestCameraPermission() {
        Log.i(TAG, "相机权限还没有被授予.现在请求权限.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // 如果以前已经请求过授权，根据是否继续询问来显示授权对话框.
            Log.i(TAG,
                    "显示相机授权申请.");
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            // 相机权限还没有被授予，直接请求.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }

    /**
     * 当'显示相机预览'按钮被点击时调用,回调定义在布局资源文件中.
     */
    public void showContacts(View v) {
        Log.i(TAG, "联系人按钮被点击时显示,现在检查权限.");

        // 校验所有的联系人权限是否被授予.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "联系人权限还没有被授予,现在请求权限.");
            requestContactsPermissions();

        } else {
            // 联系人权限已经被授予，显示联系人fragment.
            Log.i(TAG,
                    "联系人权限已经被授予，显示联系人详细信息.");
            showContactDetails();
        }
    }

    /**
     * 请求联系人权限.如果权限以前被拒绝，一个SnackBar会被显示给用户请求授权，否则直接请求.
     */
    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CONTACTS)) {

            // 如果以前已经请求过授权，根据是否继续询问来显示授权对话框.
            Log.i(TAG, "显示请求联系人权限申请.");

            // 显示一个SnackBar提示用户授权.
            Snackbar.make(mLayout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(MainActivity.this, PERMISSIONS_CONTACT,
                                            REQUEST_CONTACTS);
                        }
                    })
                    .show();
        } else {
            // 如果联系人权限还没有被授予，直接请求.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }


    /**
     * 如果需要的相机权限已经授予时显示.
     */
    private void showCameraPreview() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("contacts")
                .commit();
    }

    /**
     * 如果需要的联系人权限已经授予时显示.
     */
    private void showContactDetails() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, ContactsFragment.newInstance())
                .addToBackStack("contacts")
                .commit();
    }


    /**
     * 当权限请求完成时的回调函数.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // 接收请求相机权限的结果.
            Log.i(TAG, "接收相机权限请求的回应.");
            // 检查请求的权限是否被授予.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 如果相机权限已经被授予，则显示相机预览.
                Log.i(TAG, "相机权限已经被授予，显示相机预览.");
                Snackbar.make(mLayout, R.string.permision_available_camera,
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "相机权限被拒绝.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)

        } else if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "接收联系人权限请求的回应.");
            // 联系人的权限是多个，所以这里检查所有的联系人权限.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // 如果所有的联系人权限被授予，显示联系人fragment.
                Snackbar.make(mLayout, R.string.permision_available_contacts,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Log.i(TAG, "联系人权限被拒绝.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }


}
