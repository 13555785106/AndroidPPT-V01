package com.telecom.highdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_01:
                        startActivity(new Intent(MainActivity.this, SurfaceActivity.class));
                        break;
                    case R.id.button_02:
                        startActivity(new Intent(MainActivity.this, TextureActivity.class));
                        break;
                }
            }
        };
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View v = mGridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(clickListener);
            }
        }
    }
}
