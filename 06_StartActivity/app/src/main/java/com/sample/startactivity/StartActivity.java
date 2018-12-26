package com.sample.startactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    private TextView mResultAddTextView;
    private TextView mResultSubTextView;
    private Button mButtonAdd;
    private Button mButtonSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mResultAddTextView = findViewById(R.id.textview_result_add);
        mResultSubTextView = findViewById(R.id.textview_result_sub);
        mButtonAdd = findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, AddActivity.class);
                intent.putExtra("num01", 5);
                intent.putExtra("num02", 1);
                startActivityForResult(intent, 0);
            }
        });
        mButtonSub = findViewById(R.id.button_sub);
        mButtonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SubActivity.class);
                intent.putExtra("num01", 9);
                intent.putExtra("num02", 4);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra("result", 0);
                mResultAddTextView.setText("" + result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                mResultAddTextView.setText("结果被取消");
            }
        }
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra("result", 0);
                mResultSubTextView.setText("" + result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                mResultSubTextView.setText("结果被取消");
            }
        }
    }
}
