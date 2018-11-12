package com.sample.apptest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ViewPagerActivity extends AppCompatActivity {
    ViewPager mViewPager;
    int[] mImageIds = new int[]{R.drawable.girl_00, R.drawable.girl_01, R.drawable.girl_02, R.drawable.girl_03, R.drawable.girl_04, R.drawable.girl_05};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SimplePageAdapter(this, mImageIds));
    }


    static class SimplePageAdapter extends PagerAdapter {
        Context mContext;
        int[] mImageIds;

        SimplePageAdapter(Context context, int[] imageIds) {
            mContext = context;
            mImageIds = imageIds;
        }

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
            layoutParams.width =MATCH_PARENT;
            layoutParams.height=MATCH_PARENT;
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageDrawable(mContext.getResources().getDrawable(mImageIds[position], null));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof View) {
                View v = (View) object;
                container.removeView(v);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "标题" + position;
        }

    }
}
