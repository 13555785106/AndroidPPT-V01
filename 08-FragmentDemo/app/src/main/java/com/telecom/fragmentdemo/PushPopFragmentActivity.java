package com.telecom.fragmentdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PushPopFragmentActivity extends AppCompatActivity {
    FragmentManager mFragmentManager;
    TextView mCounterTextView;
    TextView mMsgTextView;
    Button mPushButton;
    Button mPopButton;
    int index = -1;
    Fragment[] mFragments = new Fragment[]{
            BananaFragment.newInstance(),
            KiwiFragment.newInstance(),
            MangoFragment.newInstance(),
            OrangeFragment.newInstance(),
            StrawberryFragment.newInstance(),
            WatermelonFragment.newInstance()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_pop_fragment);
        mFragmentManager = getSupportFragmentManager();
        mCounterTextView = (TextView) findViewById(R.id.counter_text_view);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mPushButton = (Button) findViewById(R.id.button_push);
        mPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < mFragments.length - 1) {
                    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mFragmentTransaction.replace(R.id.fragment_placeholder, mFragments[++index]);
                    mFragmentTransaction.addToBackStack(null);
                    mFragmentTransaction.commit();
                    mFragmentManager.executePendingTransactions();
                    updateMsg();
                } else
                    Snackbar.make(v, "已到达最顶部", Snackbar.LENGTH_LONG).show();
            }
        });
        mPopButton = (Button) findViewById(R.id.button_pop);
        mPopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > -1) {
                    mFragmentManager.popBackStack();
                    mFragmentManager.executePendingTransactions();
                    index--;
                    updateMsg();
                } else
                    Snackbar.make(v, "已到达最底部", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMsg();
    }

    private void updateMsg() {
        mCounterTextView.setText(Integer.toString(index));
        mMsgTextView.setText(new ViewHiberarchyPrinter(getWindow().getDecorView()).toString());
    }
}
