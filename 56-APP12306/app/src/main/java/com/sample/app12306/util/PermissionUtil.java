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

package com.sample.app12306.util;

import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * 用来包装访问运行时权限的工具类提供基本的帮助函数。
 */
public abstract class PermissionUtil {

    /**
     * 检测给定的权限组是否已经授权.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // grantResults长度至少为1.
        if (grantResults.length < 1) {
            return false;
        }

        // 验证每一个需要的权限是否已经授权，否则返回false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
