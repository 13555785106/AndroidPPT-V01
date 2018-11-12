package com.sample.apptest;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class LayoutInflaterActivity extends AppCompatActivity {
    private final static String TAG = LayoutInflaterActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_inflater);
        ConstraintLayout rootConstraintLayout = findViewById(R.id.root_layout);
        LayoutInflater inflater = LayoutInflater.from(this);
        //View view = inflater.inflate(R.layout.simple_layout, rootConstraintLayout,true);
        //View view = inflater.inflate(R.layout.simple_layout, rootConstraintLayout,false);
        View view = inflater.inflate(R.layout.simple_layout, null,false);
        rootConstraintLayout.addView(view);
        Log.e(TAG,new ViewHiberarchyPrinter(rootConstraintLayout).toString());
    }
}
