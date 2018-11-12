package com.telecom.dialogdemo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class DialogActivity extends AppCompatActivity {
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {
            String[] countries = {"中国", "日本", "美国", "欧洲"};
            boolean[] multihoices = new boolean[]{false, false, false, false};
            int singleChoice = -1;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_01:
                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(DialogActivity.this);
                        normalDialog.setIcon(R.mipmap.ic_launcher);
                        normalDialog.setTitle("我是一个普通Dialog");
                        normalDialog.setMessage("你要点击哪一个按钮呢?");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(mGridLayout, "你点击了\"确定\"按钮", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                        normalDialog.setNeutralButton("中立",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(mGridLayout, "你点击了\"中立\"按钮", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                        normalDialog.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(mGridLayout, "你点击了\"取消\"按钮", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                        View view = normalDialog.show().getWindow().getDecorView();
                        ViewHiberarchyPrinter viewHiberarchyPrinter = new ViewHiberarchyPrinter(view);
                        System.out.println(viewHiberarchyPrinter);
                        break;
                    case R.id.button_02:

                        AlertDialog.Builder listDialog = new AlertDialog.Builder(DialogActivity.this);
                        listDialog.setTitle("我是一个列表Dialog");
                        listDialog.setItems(countries, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(mGridLayout, "你选择了 " + countries[which], Snackbar.LENGTH_LONG).show();
                            }
                        });
                        listDialog.show();
                        break;
                    case R.id.button_03:

                        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(DialogActivity.this);
                        singleChoiceDialog.setTitle("我是一个单选Dialog");
                        singleChoiceDialog.setSingleChoiceItems(countries, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                singleChoice = which;
                            }
                        });
                        singleChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (singleChoice != -1) {
                                    Snackbar.make(mGridLayout, "你选择了 " + countries[singleChoice], Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                        singleChoiceDialog.show();
                        break;
                    case R.id.button_04:
                        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(DialogActivity.this);
                        multiChoiceDialog.setTitle("我是一个多选Dialog");
                        multiChoiceDialog.setMultiChoiceItems(countries, multihoices, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                multihoices[which] = isChecked;
                            }
                        });
                        multiChoiceDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StringBuilder stringBuilder = new StringBuilder(" ");
                                        for (int i = 0; i < countries.length; i++) {
                                            if (multihoices[i])
                                                stringBuilder.append(countries[i] + " ");
                                        }
                                        Snackbar.make(mGridLayout, "你选择了 " + stringBuilder.toString(), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                        multiChoiceDialog.show();
                        break;
                    case R.id.button_05:
                        ProgressDialog waitingDialog = new ProgressDialog(DialogActivity.this);
                        waitingDialog.setTitle("我是一个等待Dialog");
                        waitingDialog.setMessage("等待中...");
                        waitingDialog.setIndeterminate(true);
                        waitingDialog.setCancelable(true);
                        waitingDialog.show();
                        break;
                    case R.id.button_06:
                        final ProgressDialog progressDialog = new ProgressDialog(DialogActivity.this);
                        progressDialog.setProgress(0);
                        progressDialog.setTitle("我是一个进度条Dialog");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int progress = 0;
                                while (progress < 100) {
                                    try {
                                        Thread.sleep(100);
                                        progress++;
                                        progressDialog.setProgress(progress);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                progressDialog.cancel();
                            }
                        }).start();
                        break;
                    case R.id.button_07:
                        LinearLayout.LayoutParams linearLayoutParams = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout linearLayout = new LinearLayout(DialogActivity.this);
                        linearLayout.setLayoutParams(linearLayoutParams);
                        ImageView imageView = new ImageView(DialogActivity.this);
                        imageView.setImageResource(R.mipmap.ic_launcher_round);
                        final EditText editText = new EditText(DialogActivity.this);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        editText.setLayoutParams(params);
                        editText.setText("约翰");
                        editText.setHint("请输入你的姓名");
                        linearLayout.addView(imageView);
                        linearLayout.addView(editText);
                        AlertDialog.Builder inputDialog = new AlertDialog.Builder(DialogActivity.this);
                        inputDialog.setTitle("我是一个编辑Dialog").setView(linearLayout);
                        inputDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Snackbar.make(mGridLayout, "你的姓名是 " + editText.getText().toString(), Snackbar.LENGTH_LONG).show();
                                    }
                                }).show();
                        break;
                    case R.id.button_08:
                        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(DialogActivity.this);
                        final View dialogView = LayoutInflater.from(DialogActivity.this).inflate(R.layout.customized_dialog, null);
                        customizeDialog.setTitle("我是一个自定义Dialog");
                        customizeDialog.setIcon(R.drawable.message_clouds_64);
                        customizeDialog.setView(dialogView);
                        customizeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText accountEditText = dialogView.findViewById(R.id.account_edit_text);
                                EditText passwordEditText = dialogView.findViewById(R.id.password_edit_text);
                                Snackbar.make(mGridLayout, "你输入的账号是 " +
                                                accountEditText.getText().toString() +
                                                ",密码是 " +
                                                passwordEditText.getText().toString()
                                        , Snackbar.LENGTH_LONG).show();
                            }
                        });
                        customizeDialog.show();
                        break;
                    case R.id.button_09:

                        DatePickerDialog datePickerDialog = new DatePickerDialog(DialogActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                System.out.println(year + "-" + month + "-" + dayOfMonth);
                            }
                        }, 2018, 2, 25);
                        datePickerDialog.show();

                        break;
                    case R.id.button_10:
                        TimePickerDialog timePickerDialog = new TimePickerDialog(DialogActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                System.out.println(hourOfDay + ":" + minute);
                            }
                        }, 10, 10, true);
                        timePickerDialog.show();
                        break;
                    case R.id.button_11:
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DialogActivity.this);
                        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                        bottomSheetDialog.show();
                        break;

                }
            }
        };
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(clickListener);
            }
        }
    }
}
