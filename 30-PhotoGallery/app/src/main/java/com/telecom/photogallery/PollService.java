package com.telecom.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class PollService extends IntentService {
    public static final String ACTION_SHOW_NOTIFICATION = "com.telecom.photogallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.telecom.photogallery.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private static final String baseUrl = "http://192.168.3.50:8080/PhotoGallery/ListImage";

    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        System.out.println(POLL_INTERVAL_MS);
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        QueryPreferences.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("服务进程id = " + android.os.Process.myPid());


        String query = QueryPreferences.getStoredQuery(this);
        String resultCount = QueryPreferences.getResultCount(this);
        String firstResultId = QueryPreferences.getFirstResultId(this);
        String lastResultId = QueryPreferences.getLastResultId(this);
        System.out.println("resultCount=" + resultCount);
        System.out.println("firstResultId=" + firstResultId);
        System.out.println("lastResultId=" + lastResultId);
        PhotoResult photoResult = PhotoFetchr.fetchPhotoItems(baseUrl, query, "UTF-8");
        if (photoResult.getDatas().size() == 0)
            return;
        List<PhotoItem> photoItems = photoResult.getDatas();
        String rCount = Long.toString(photoItems.size());
        String fId = Long.toString(photoItems.get(0).getId());
        String lId = Long.toString(photoItems.get(photoResult.getDatas().size() - 1).getId());
        System.out.println("rCount=" + rCount);
        System.out.println("fId=" + fId);
        System.out.println("lId=" + lId);
        if (rCount.equals(resultCount) && fId.equals(firstResultId) && lId.equals(lastResultId)) {
            Log.i(TAG, "获取到旧数据");
        } else {
            Log.i(TAG, "获取到新数据");
            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            showBackgroundNotification(0, notification);

            QueryPreferences.setResultCount(this, rCount);
            QueryPreferences.setFirstResultId(this, fId);
            QueryPreferences.setLastResultId(this, lId);
        }
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }
}
