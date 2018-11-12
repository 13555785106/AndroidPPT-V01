package com.telecom.orderedbroadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OrderedBroadcastReceiver01 extends BroadcastReceiver {

    private static final String TAG = "OBR01";

    @Override
    public void onReceive(Context c, Intent intent) {
        Log.i(TAG, "received result: " + getResultCode());
        IntentUtils.printIntentInfo(intent);
        Log.i(TAG,getResultExtras(true).getString("name"));
        getResultExtras(true).putString("name","约翰.琼斯");
        getResultExtras(true).putInt("age",36);
        setResultCode(Activity.RESULT_CANCELED);
    }
}
