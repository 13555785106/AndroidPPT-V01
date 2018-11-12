package com.sample.app12306.http;

import android.os.Handler;
import android.os.Message;

import com.sample.app12306.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaojf on 18/9/23.
 */

public class RequestThread implements Runnable {
    private String mAction;
    private Map<String, List<String>> mReqParams;
    private Handler mHandler;

    public RequestThread(Handler handler, String action, Map<String, List<String>> reqParams) {
        mHandler = handler;
        mAction = action;
        mReqParams = reqParams;
    }

    @Override
    public void run() {
        String ret = HttpUtils.utf8PostRequest(Constants.REMOTE_SERVER_ROOT_URL + mAction, mReqParams);
        Message message = Message.obtain();
        message.what = Integer.MAX_VALUE;
        message.getData().putString("response", ret);
        mHandler.sendMessage(message);
    }
}
