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

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RuntimePermissionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, null);

        // BEGIN_INCLUDE(m_only_permission)
        if (Build.VERSION.SDK_INT < 23) {
            /*
            对于Android M及其以上平台，联系人权限已经在AndroidManifest声明了.
            在以前的平台这个权限是不可用的，所以我们隐藏了联系人数据库的按钮.
            这个权限可以在运行时添加，但是不能应用在旧平台.
             */
            root.findViewById(R.id.button_contacts).setVisibility(View.GONE);
        }
        // END_INCLUDE(m_only_permission)

        return root;
    }
}
