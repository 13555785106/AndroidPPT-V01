package com.telecom.resourcedemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mMsgTextView;
    Button mButtonA;
    Button mButtonB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        for (String country : getResources().getStringArray(R.array.countries)) {
            mMsgTextView.append(country + " ");
        }
        mButtonA = (Button) findViewById(R.id.button_a);
        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AssetManager assetManager = getAssets();
                    AssetFileDescriptor afd = assetManager.openFd("airplane.wav");
                    MediaPlayer player = new MediaPlayer();
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    player.setLooping(false);
                    player.prepare();
                    player.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mButtonB = (Button) findViewById(R.id.button_b);
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.car);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                player.start();
            }
        });
    }
}
