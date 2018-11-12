package com.sample.optimizememory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {
            long timeStart, timeEnd, timeInterval;
            String str;
            int loopCount = 100000;
            List<Integer> intList;
            long sum = 0;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_01:
                        timeStart = System.nanoTime();
                        HashMap<Integer, String> hm = new HashMap<Integer, String>();
                        for (int i = 0; i < loopCount; i++) {
                            hm.put(i, "" + i);
                        }
                        str = hm.get(0);
                        timeEnd = System.nanoTime();
                        timeInterval = timeEnd - timeStart;
                        Log.e(TAG, "HashMap<Integer, String> 耗时 " + timeInterval);
                        break;
                    case R.id.button_02:
                        //牺牲时间节约内存空间
                        timeStart = System.nanoTime();
                        SparseArray<String> sa = new SparseArray<String>();
                        for (int i = 0; i < loopCount; i++) {
                            sa.put(i, "" + i);
                        }
                        str = sa.get(0);
                        timeEnd = System.nanoTime();
                        timeInterval = timeEnd - timeStart;
                        Log.e(TAG, "SparseArray<String> 耗时 " + timeInterval);
                        break;
                    case R.id.button_03:
                        intList = new ArrayList<>();
                        for (int i = 0; i < loopCount; i++) {
                            intList.add(i);
                        }
                        sum = 0;
                        timeStart = System.nanoTime();
                        for (int i = 0; i < intList.size(); i++) {
                            sum += intList.get(i);
                        }
                        timeEnd = System.nanoTime();
                        timeInterval = timeEnd - timeStart;
                        Log.e(TAG, "for(每次调用size函数)耗时 " + timeInterval);
                        break;
                    case R.id.button_04:
                        intList = new ArrayList<>();
                        for (int i = 0; i < loopCount; i++) {
                            intList.add(i);
                        }
                        sum = 0;
                        timeStart = System.nanoTime();
                        int size = intList.size();
                        for (int i = 0; i < size; i++) {
                            sum += intList.get(i);
                        }
                        timeEnd = System.nanoTime();
                        timeInterval = timeEnd - timeStart;
                        Log.e(TAG, "for(一次调用size函数)耗时 " + timeInterval);
                        break;
                    case R.id.button_05:
                        startActivity(new Intent(MainActivity.this, ActivityLeak1.class));
                        break;
                    case R.id.button_06:
                        startActivity(new Intent(MainActivity.this, ActivityLeak2.class));
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
