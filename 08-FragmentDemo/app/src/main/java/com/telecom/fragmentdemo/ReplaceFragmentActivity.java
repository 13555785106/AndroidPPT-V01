package com.telecom.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReplaceFragmentActivity extends AppCompatActivity {
    GridLayout mGridLayout;
    TextView mMsgTextView;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_fragment);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mFragmentManager = getSupportFragmentManager();

        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                        switch (v.getId()) {
                            case R.id.button_banana:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, BananaFragment.newInstance(), "BananaFragment");
                                break;
                            case R.id.button_kiwi:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, KiwiFragment.newInstance(), "KiwiFragment");
                                break;
                            case R.id.button_mango:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, MangoFragment.newInstance(), "MangoFragment");
                                break;
                            case R.id.button_orange:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, OrangeFragment.newInstance(), "OrangeFragment");
                                break;
                            case R.id.button_strawberry:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, StrawberryFragment.newInstance(), "StrawberryFragment");
                                break;
                            case R.id.button_watermelon:
                                mFragmentTransaction.replace(R.id.fragment_placeholder, WatermelonFragment.newInstance(), "WatermelonFragment");
                                break;
                        }
                        mFragmentTransaction.commitNow();
                        mMsgTextView.setText(new ViewHiberarchyPrinter(getWindow().getDecorView()).toString());
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMsgTextView.setText(new ViewHiberarchyPrinter(getWindow().getDecorView()).toString());
    }
}
