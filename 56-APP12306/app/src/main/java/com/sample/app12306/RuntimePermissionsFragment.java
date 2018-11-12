package com.sample.app12306;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RuntimePermissionsFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS = 0;
    private static String[] RUNTIME_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE};
    TextView mTextViewMessage;
    Button mButtonStart;
    private List<String> mNotGrantedPermissions = new ArrayList<>();
    private List<String> mNotToAskPermissions = new ArrayList<>();
    private int mNotGrantedPermissionsAmount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_runtime_permissions, container, false);
        mTextViewMessage = v.findViewById(R.id.text_view_message);
        mButtonStart = v.findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println(Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : RUNTIME_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    mNotGrantedPermissions.add(permission);
                }
            }
            for (String permission : mNotGrantedPermissions) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_PERMISSIONS);
            }
        }
        mNotGrantedPermissionsAmount = mNotGrantedPermissions.size();
        if (mNotGrantedPermissionsAmount == 0) {
            mButtonStart.callOnClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            mNotGrantedPermissionsAmount--;
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mNotGrantedPermissions.remove(permissions[0]);
            else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]))
                    mNotToAskPermissions.add(permissions[0]);
            }


            if (mNotGrantedPermissionsAmount == 0) {
                if (mNotGrantedPermissions.size() > 0) {
                    mTextViewMessage.append("存在未授予的权限，某些功能将不能使用！\r\n\r\n");
                    mTextViewMessage.append("以下是被拒绝的权限：\r\n");
                    for (String permission : mNotGrantedPermissions) {
                        mTextViewMessage.append("\t" + permission + "\r\n");
                    }
                    if (mNotToAskPermissions.size() > 0) {
                        mTextViewMessage.append("以下是只能在设置中重新开启的权限：" + "\r\n");
                        for (String permission : mNotToAskPermissions) {
                            mTextViewMessage.append("\t" + permission + "\r\n");
                        }
                    }
                } else {
                    mButtonStart.callOnClick();
                }
            }
        }
    }
}
