package com.telecom.handlerlooperdemo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.text.DecimalFormat;

/**
 * Created by xiaojf on 18/2/5.
 */

public class CalculateTask implements Runnable {
    private Handler mInnerHandler = null;
    private Handler mOuterHandler = null;
    private Looper mLooper = null;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    public CalculateTask(Handler outerHandler) {
        mOuterHandler = outerHandler;
    }

    public void run() {
        if (mOuterHandler != null) {
            Message message = Message.obtain();
            message.what = 0;
            mOuterHandler.sendMessage(message);
        }
        Looper.prepare();
        if (mLooper == null)
            mLooper = Looper.myLooper();
        mInnerHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String operator = msg.getData().getString("operator");
                        double firstValue = msg.getData().getDouble("first_value");
                        double secondValue = msg.getData().getDouble("second_value");
                        String resultStr = "计算错误!";
                        try {
                            double result = 0;
                            if (operator.equals("+")) {
                                result = firstValue + secondValue;
                            } else if (operator.equals("-")) {
                                result = firstValue - secondValue;
                            } else if (operator.equals("*")) {
                                result = firstValue * secondValue;
                            } else if (operator.equals("/")) {
                                result = firstValue / secondValue;
                            }
                            resultStr = mDecimalFormat.format(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mOuterHandler != null) {
                            Message message = Message.obtain();
                            message.what = 1;
                            message.getData().putString("result", resultStr);
                            mOuterHandler.sendMessage(message);
                        }
                        break;
                    case 1:
                        mLooper.quitSafely();
                        break;
                }
            }
        };
        Looper.loop();
        if (mOuterHandler != null) {
            Message message = Message.obtain();
            message.what = 2;
            mOuterHandler.sendMessage(message);
        }
    }

    public Handler getHandler() {
        return mInnerHandler;
    }
}
