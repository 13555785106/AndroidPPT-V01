package com.telecom.layoutdemo;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TabLayoutActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ImageView mImageView;
    int [] imageResIds = new int[]{
            R.drawable.bird_00,
            R.drawable.bird_01,
            R.drawable.bird_02,
            R.drawable.bird_03,
            R.drawable.bird_04
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        for(int i=0;i<mTabLayout.getTabCount();i++){
            mTabLayout.getTabAt(i).setIcon(imageResIds[i]);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mImageView.setImageResource(imageResIds[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mImageView = (ImageView)findViewById(R.id.image_view);
    }
}
