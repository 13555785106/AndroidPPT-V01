package com.telecom.projecttemplet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    GridLayout mGridLayout;
    View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (v.getId()){
                    case R.id.button_empty_activity:
                        intent = new Intent(MainActivity.this,EmptyActivity.class);
                        break;
                    case R.id.button_basic_activity:
                        intent = new Intent(MainActivity.this,BasicActivity.class);
                        break;
                    case R.id.button_bottom_navigation_activity:
                        intent = new Intent(MainActivity.this,BottomNavigationActivity.class);
                        break;
                    case R.id.button_fullscreen_activity:
                        intent = new Intent(MainActivity.this,FullscreenActivity.class);
                        break;

                    case R.id.button_master_detail_activity:
                        intent = new Intent(MainActivity.this,MasterDetailActivityListActivity.class);
                        break;
                    case R.id.button_navigation_drawer_activity:
                        intent = new Intent(MainActivity.this,NavigationDrawerActivity.class);
                        break;
                    case R.id.button_scrolling_activity:
                        intent = new Intent(MainActivity.this,ScrollingActivity.class);
                        break;
                    case R.id.button_settings_activity:
                        intent = new Intent(MainActivity.this,SettingsActivity.class);
                        break;
                    case R.id.button_tabbed_activity:
                        intent = new Intent(MainActivity.this,TabbedActivity.class);
                        break;
                }
                if(intent != null)
                    startActivity(intent);
            }
        };
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(mOnClickListener);
            }
        }
    }
}
