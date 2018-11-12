package com.telecom.viewpagerdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ViewPagerActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TabLayout mTabLayout;
    PagerAdapter mPagerAdapter;
    ImageView[] mImageViews = new ImageView[5];
    String[] mTitles = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        for (int i = 0; i < 5; i++) {
            mTitles[i] = "girl_0" + i;
            mImageViews[i] = new ImageView(ViewPagerActivity.this);
            mImageViews[i].setImageResource(getImageResourceId(i));
            mImageViews[i].setLayoutParams(layoutParams);
            mImageViews[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == mImageViews[Integer.parseInt(object.toString())];
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mImageViews[position]);
                return position;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

        };
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(Color.BLACK, Color.RED);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setIcon(mImageViews[i].getDrawable());
        }
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        "你好，" + mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getText() + "!",
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private int getImageResourceId(int index) {
        return getResources()
                .getIdentifier(mTitles[index], "drawable", getPackageName());
    }
}
