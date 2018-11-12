package com.telecom.drawablexml;

import android.app.Activity;
import android.os.Bundle;

public class RoundedActivity extends Activity {
    //背景为圆角矩形
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rounded);
    }
}