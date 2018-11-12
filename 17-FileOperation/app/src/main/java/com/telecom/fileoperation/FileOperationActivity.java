package com.telecom.fileoperation;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileOperationActivity extends AppCompatActivity {


    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    TextView mMsgTextView;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operation);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        findViewById(R.id.button_get_files_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getFilesDir();
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_get_file_stream_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getFileStreamPath("1.txt");
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_get_cache_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getCacheDir();
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_get_code_cache_dir).setOnClickListener(new View.OnClickListener() {
            @TargetApi(21)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21) {
                    File file = getCodeCacheDir();
                    try {
                        mMsgTextView.setText(file.getCanonicalPath());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else
                    mMsgTextView.setText("Need API 21 ！");
            }
        });
        findViewById(R.id.button_get_data_dir).setOnClickListener(new View.OnClickListener() {
            @TargetApi(24)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 24) {
                    File file = getDataDir();
                    try {
                        mMsgTextView.setText(file.getCanonicalPath());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else
                    mMsgTextView.setText("Need API 24 ！");
            }
        });
        findViewById(R.id.button_get_database_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getDatabasePath("1.db");
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_get_package_code_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = getPackageCodePath();
                mMsgTextView.setText(file);
                getPackageResourcePath();
            }
        });

        findViewById(R.id.button_get_package_resource_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = getPackageResourcePath();
                mMsgTextView.setText(file);
            }
        });
        findViewById(R.id.button_get_external_files_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getExternalFilesDir(null);
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        findViewById(R.id.button_get_external_cache_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getExternalCacheDir();
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });


        findViewById(R.id.button_get_external_file_dirs_directory_movies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                if (Build.VERSION.SDK_INT >= 19) {
                    File[] files = getExternalFilesDirs(Environment.DIRECTORY_MOVIES);
                    try {
                        for (File file : files) {
                            stringBuilder.append(file.getCanonicalPath());
                            stringBuilder.append("\n");
                        }
                        mMsgTextView.setText(stringBuilder.toString());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }else
                    mMsgTextView.setText("需要 API19");
            }
        });
        findViewById(R.id.button_get_external_files_dirs_directory_documents).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                if (Build.VERSION.SDK_INT >= 19) {
                File[] files = getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS);
                try {
                    for(File file : files) {
                        stringBuilder.append(file.getCanonicalPath());
                        stringBuilder.append("\n");
                    }
                    mMsgTextView.setText(stringBuilder.toString());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                }else
                    mMsgTextView.setText("需要 API19");
            }
        });
        findViewById(R.id.button_environment_get_external_storage_directory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = Environment.getExternalStorageDirectory();
                try {
                    mMsgTextView.setText(file.getCanonicalPath());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        //getExternalCacheDir();
        //getExternalCacheDirs();
        //getExternalFilesDir("");
        //getExternalFilesDirs("");
        //getExternalMediaDirs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {

        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
