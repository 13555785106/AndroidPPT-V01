package com.telecom.notifications;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RadioGroup;

public class NotificationActivity extends Activity {

    private RadioGroup mOptionsGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mOptionsGroup = (RadioGroup) findViewById(R.id.options_group);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        
//        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);
//    }

    public void onPostClick(View v) {
        final int noteId = mOptionsGroup.getCheckedRadioButtonId();
        final Notification note;
        switch (noteId) {
            case R.id.option_basic:
            case R.id.option_bigtext:
            case R.id.option_bigpicture:
            case R.id.option_inbox:
                note = buildStyledNotification(noteId);
                break;
            case R.id.option_private:
            case R.id.option_secret:
            case R.id.option_headsup:
                note = buildSecuredNotification(noteId);
                break;
            default:
                throw new IllegalArgumentException("Unknown Type");
        }

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(noteId, note);
    }

    private Notification buildStyledNotification(int type) {
        Intent launchIntent =
                new Intent(this, NotificationActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, launchIntent, 0);

        // Create notification with the time it was fired
        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationActivity.this);


        builder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Something Happened")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("We're Finished!")
                .setContentText("Click Here!")
                .setContentIntent(contentIntent);

        switch (type) {
            case R.id.option_basic:
                //返回简单通知
                return builder.build();
            case R.id.option_bigtext:
                //包含两个动作
                builder.addAction(android.R.drawable.ic_menu_call,
                        "呼叫", contentIntent);
                builder.addAction(android.R.drawable.ic_menu_recent_history,
                        "历史", contentIntent);
                //展开时使用BigTextStyle样式
                NotificationCompat.BigTextStyle textStyle =
                        new NotificationCompat.BigTextStyle(builder);
                textStyle.bigText("Here is some additional text to be displayed when the notification is "
                        + "in expanded mode.  I can fit so much more content into this giant view!");

                return textStyle.build();
            case R.id.option_bigpicture:
                //添加一个附加的动作
                builder.addAction(android.R.drawable.ic_menu_compass,
                        "查看位置", contentIntent);
                ///展开时使用BigTextStyle样式
                NotificationCompat.BigPictureStyle pictureStyle =
                        new NotificationCompat.BigPictureStyle(builder);
                pictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.dog));

                return pictureStyle.build();
            case R.id.option_inbox:
                //展开时使用 InboxStyle 样式
                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle(builder);
                inboxStyle.setSummaryText("4 个新任务");
                inboxStyle.addLine("做午饭");
                inboxStyle.addLine("给妈妈打电话");
                inboxStyle.addLine("给老婆打电话");
                inboxStyle.addLine("接孩子");

                return inboxStyle.build();
            default:
                throw new IllegalArgumentException("Unknown Type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    //These properties can be overridden by the user's notification settings
    private Notification buildSecuredNotification(int type) {
        Intent launchIntent = new Intent(this, NotificationActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, launchIntent, 0);

        //构造基本通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("账户余额更新")
                .setContentText("你的账户余额是 -$250.00")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("你的账户余额是 -$250.00; 请主动付款给我们或者法律强制执行！ "))
                .setContentIntent(contentIntent);

        switch (type) {
            case R.id.option_private:
                Notification publicNote = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("账号通知")
                        .setContentText("一条重要的消息已经到达.")
                        .setContentIntent(contentIntent)
                        .build();

                return builder.setPublicVersion(publicNote)
                        .build();
            case R.id.option_secret:
                //锁屏时隐藏
                return builder.setVisibility(Notification.VISIBILITY_SECRET)
                        .build();
            case R.id.option_headsup:
                //高优先级的通知
                return builder.setDefaults(Notification.DEFAULT_SOUND)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build();
            default:
                throw new IllegalArgumentException("Unknown Type");
        }
    }
}