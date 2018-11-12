package com.telecom.drawoncanvas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


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
                        startActivity(new Intent(MainActivity.this,Sample01Activity.class));
                        break;
                    case R.id.button_02:
                        startActivity(new Intent(MainActivity.this,Sample02Activity.class));
                        break;
                    case R.id.button_03:
                        startActivity(new Intent(MainActivity.this,Sample03Activity.class));
                        break;
                    case R.id.button_04:
                        startActivity(new Intent(MainActivity.this,Sample04Activity.class));
                        break;
                    case R.id.button_05:
                        startActivity(new Intent(MainActivity.this,Sample05Activity.class));
                        break;
                    case R.id.button_06:
                        startActivity(new Intent(MainActivity.this,Sample06Activity.class));
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
        ImageView imageView =(ImageView) findViewById(R.id.image_view);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("pic01.jpg"));
            imageView.setImageBitmap(bitmap);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }
}
