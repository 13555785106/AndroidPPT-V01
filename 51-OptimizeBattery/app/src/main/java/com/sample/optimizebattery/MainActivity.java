package com.sample.optimizebattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


// Intent.ACTION_BATTERY_CHANGED 充电状态，或者电池的电量发生变化 只有通过Context.registerReceiver()注册
// Intent.ACTION_BATTERY_LOW 表示电池电量低
// Intent.ACTION_BATTERY_OKAY 表示电池电量充足，即从电池电量低变化到饱满时会发出广播
// Intent.ACTION_POWER_CONNECTED 插上外部电源时发出的广播
// Intent.ACTION_POWER_DISCONNECTED 已断开外部电源连接时发出的广播
public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    Button mButtonObtainBatteryStatusInfo;
    TextView mTextViewMsg;
    private BroadcastReceiver mBatteryChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IntentUtils.printIntentInfo(intent);
            mTextViewMsg.setText(getBatteyInfo(intent));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonObtainBatteryStatusInfo = findViewById(R.id.button_obtain_battery_status_info);
        mTextViewMsg = findViewById(R.id.text_view_msg);
        mButtonObtainBatteryStatusInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent intentBatteryStatus = registerReceiver(null, intentFilter);
                Log.e(TAG, "============================================");
                IntentUtils.printIntentInfo(intentBatteryStatus);
                mTextViewMsg.setText(getBatteyInfo(intentBatteryStatus));

            }
        });

        registerReceiver(mBatteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private String getBatteyInfo(Intent intentBatteryStatus) {
        StringBuilder sb = new StringBuilder();
        int level = intentBatteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intentBatteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        sb.append("电量：" + level + "/" + scale + "\r\n");

        int status = intentBatteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        sb.append("充电中：");
        if (isCharging)
            sb.append("是\r\n");
        else
            sb.append("否\r\n");
        int chargePlug = intentBatteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isUSBCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        sb.append("USB充电：");
        if (isUSBCharging)
            sb.append("是\r\n");
        else
            sb.append("否\r\n");
        boolean isACCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        sb.append("电源充电：");
        if (isACCharging)
            sb.append("是\r\n");
        else
            sb.append("否\r\n");
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBatteryChangedReceiver);
        super.onDestroy();
    }

}
