package com.telecom.notifications;

import android.annotation.SuppressLint;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("NewApi")
public class MonitorService extends NotificationListenerService {
    private static final String TAG = "RecipesMonitorService";
    
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //确认通知是否来自本应用程序
        if (!TextUtils.equals(sbn.getPackageName(), getPackageName())) {
            return;
        }
        
        Log.i(TAG, "Notification "+sbn.getId()+" Posted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //确认通知是否来自本应用程序
        if (!TextUtils.equals(sbn.getPackageName(), getPackageName())) {
            return;
        }
        if (R.id.option_basic != sbn.getId()) {
            return;
        }
        
        //If the basic notification cancels, dismiss all of ours
        for (StatusBarNotification note : getActiveNotifications()) {
            if (TextUtils.equals(note.getPackageName(), getPackageName())) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    cancelNotification(note.getPackageName(),
                            note.getTag(),
                            note.getId());
                } else {
                    cancelNotification(note.getKey());
                }
            }
        }
    }

}
