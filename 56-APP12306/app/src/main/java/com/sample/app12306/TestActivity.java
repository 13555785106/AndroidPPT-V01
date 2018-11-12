package com.sample.app12306;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sample.app12306.http.HttpUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    Button mButtonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mButtonTest = findViewById(R.id.button_test);
        mButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String str = null;

                        Map<String, List<String>> map = new HashMap<>();
                        map.put("name", Arrays.asList(new String[]{"肖俊峰"}));
                        map.put("age", Arrays.asList(new String[]{"45"}));
                        str = HttpUtils.utf8GetRequest("http://192.168.3.9:9090/12306/HelloWorld", map);

                        System.out.println(str);
                    }
                }).start();
            }
        });
    }
}
