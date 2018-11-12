package com.sample.app12306;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sample.app12306.db.DbSchema;
import com.sample.app12306.db.StationListDac;
import com.sample.app12306.model.Station;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    private Button mButtonSkip;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0)
                skipSplash();
            String buttonSkipText = getResources().getString(R.string.button_skip_text);
            mButtonSkip.setText(String.format(buttonSkipText, msg.arg1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏模式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        mButtonSkip = findViewById(R.id.button_skip);
        mButtonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipSplash();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i >= 0; i--) {
                    Message message = Message.obtain();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                    try {
                        if (i != 0)
                            Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        String[] stations = new String[]{"北京", "唐山北", "锦州南", "辽中", "沈阳", "北戴河", "山海关", "盘锦北", "沈阳北", "开原西", "四平东", "长春", "吉林", "蛟河西", "敦化", "安图西", "延吉西", "图们北", "珲春", "秦皇岛", "铁岭西", "长春西", "扶余北", "哈尔滨西", "哈尔滨", "肇东", "安达", "大庆西", "齐齐哈尔南", "滦县", "盘锦", "海城西", "鞍山西", "辽阳", "葫芦岛北", "绥中北" };
        StationListDac stationListDac = StationListDac.get(this);
        for (String stationName : stations) {
            Station station = new Station();
            station.setStationName(stationName);
            station.setDateTime(DbSchema.timeStampFormat.format(new Date()));
            stationListDac.del(stationName);
            stationListDac.add(station);
        }
        System.out.println(stationListDac.all(""));
    }

    private void skipSplash() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
