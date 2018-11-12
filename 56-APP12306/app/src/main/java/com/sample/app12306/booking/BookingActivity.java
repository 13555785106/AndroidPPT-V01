package com.sample.app12306.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sample.app12306.R;

import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private String[] titles = new String[]{"选择订票乘客", "选择座位级别", "确认订票" };
    private FragmentManager mFragmentManager;
    private Button mButtonNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_booking);
        mFragmentManager = getSupportFragmentManager();
        nextStep();
        mButtonNextStep = findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
                int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount == 3)
                    mButtonNextStep.setText("确定");
                else
                    mButtonNextStep.setText("下一步");
            }
        });
    }

    private void nextStep() {
        int stepCount = 3;
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount < stepCount)
            setTitle(titles[backStackEntryCount]);
        Fragment fragment = null;

        if (backStackEntryCount == 0) {
            fragment = new BookingFirstStepFragment();
        } else if (backStackEntryCount == 1) {
            fragment = new BookingSecondStepFragment();
        } else if (backStackEntryCount == 2) {
            fragment = new BookingThirdStepFragment();
        }
        if (backStackEntryCount < stepCount) {
            String tag = "step" + backStackEntryCount;
            mFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
            mFragmentManager.executePendingTransactions();
        }
        onlyShowTop();

    }

    private void onlyShowTop() {
        List<Fragment> fragmentList = mFragmentManager.getFragments();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (i == fragmentList.size() - 1)
                mFragmentTransaction.show(fragment);
            else
                mFragmentTransaction.hide(fragment);
        }
        mFragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mFragmentManager.popBackStackImmediate();
                /*mFragmentManager.popBackStack();
                mFragmentManager.executePendingTransactions();*/
                int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount > 0)
                    setTitle(titles[backStackEntryCount - 1]);
                mButtonNextStep.setText("下一步");
                onlyShowTop();
                if (backStackEntryCount == 0)
                    this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
