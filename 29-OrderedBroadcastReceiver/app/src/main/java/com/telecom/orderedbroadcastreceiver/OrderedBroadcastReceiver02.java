package com.telecom.orderedbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OrderedBroadcastReceiver02 extends BroadcastReceiver {

    private static final String TAG = "OBR02";

    @Override
    public void onReceive(Context c, Intent intent) {
        Log.i(TAG, "received result: " + getResultCode());
        IntentUtils.printIntentInfo(intent);
        Log.i(TAG,getResultExtras(true).getString("name"));
        Log.i(TAG,""+getResultExtras(true).getInt("age"));
    }
}
