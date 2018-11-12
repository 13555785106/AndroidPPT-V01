package com.telecom.fragmentdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class LifeCycleActivity extends AppCompatActivity {
    private static String TAG = "生命周期 活动";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<=== onCreate ===>");
        setContentView(R.layout.activity_life_cycle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("本例用来演示Activity与Fragment的生命周期");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i(TAG, "<=== onPostCreate ===>");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "<=== onRestart ===>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "<=== onStart ===>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "<=== onResume ===>");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i(TAG, "<=== onPostResume ===>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "<=== onPause ===>");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "<=== onStop ===>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "<=== onDestroy ===>");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "<=== onRestoreInstanceState ===>");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "<=== onSaveInstanceState ===>");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "<=== onAttachedToWindow ===>");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "<=== onDetachedFromWindow ===>");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "<=== onConfigurationChanged ===>");
    }


}
