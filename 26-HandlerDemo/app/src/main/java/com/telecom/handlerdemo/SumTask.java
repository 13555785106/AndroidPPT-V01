package com.telecom.handlerdemo;

import android.os.Handler;
import android.os.Message;

/**
 * Created by xiaojf on 18/2/5.
 */

public class SumTask implements Runnable {
    int mSum = 0;
    int mStartValue = 0;
    int mEndValue = 0;
    Handler mOuterHandler;
    boolean mCanceled = false;

    public SumTask(Handler outerHandler, int startValue, int endValue) {
        this.mOuterHandler = outerHandler;
        this.mStartValue = startValue;
        this.mEndValue = endValue;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public void setCanceled(boolean canceled) {
        mCanceled = canceled;
    }

    @Override
    public void run() {
        Message msg = Message.obtain();
        msg.what = 0;
        mOuterHandler.sendMessage(msg);
        for (int i = this.mStartValue; i <= this.mEndValue; i++) {
            mSum += i;
            msg = Message.obtain();

            msg.getData().putInt("sum", mSum);
            msg.getData().putInt("percent", (int) ((i - mStartValue + 1) * 100.0 / (this.mEndValue - this.mStartValue + 1)));
            mOuterHandler.sendMessage(msg);
            if (!mCanceled) {
                msg.what = 1;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                msg.what = 3;
                return;
            }
        }
        msg = Message.obtain();
        msg.what = 2;
        msg.getData().putInt("sum", mSum);
        mOuterHandler.sendMessage(msg);
    }
}
