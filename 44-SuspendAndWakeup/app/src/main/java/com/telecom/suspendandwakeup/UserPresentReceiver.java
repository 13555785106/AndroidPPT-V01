package com.telecom.suspendandwakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UserPresentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentUtils.printIntentInfo(intent);
    }
}
