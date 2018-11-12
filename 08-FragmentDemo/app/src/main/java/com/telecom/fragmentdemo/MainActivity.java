package com.telecom.fragmentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch (v.getId()){
                            case R.id.button_life_cycle:
                                intent = new Intent(MainActivity.this,LifeCycleActivity.class);
                                Log.i(TAG,"<=== "+getResources().getString(R.string.title_activity_life_cycle)+" ===>");
                                break;
                            case R.id.button_fragment_replace:
                                intent = new Intent(MainActivity.this,ReplaceFragmentActivity.class);
                                Log.i(TAG,"<=== "+getResources().getString(R.string.title_activity_replace_fragment)+" ===>");
                                break;
                            case R.id.button_fragment_push_pop:
                                intent = new Intent(MainActivity.this,PushPopFragmentActivity.class);
                                Log.i(TAG,"<=== "+getResources().getString(R.string.title_activity_push_pop)+" ===>");
                                break;
                        }
                        if (intent != null)
                            startActivity(intent);
                    }
                });
            }
        }
    }
}
