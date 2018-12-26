package com.sample.startactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    private Button mButtonOk;
    private Button mButtonCancel;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        mTextViewResult = findViewById(R.id.textview_result);


        mButtonOk = findViewById(R.id.button_ok);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubActivity.this.finish();
            }
        });
        mButtonCancel = findViewById(R.id.button_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, null);
                SubActivity.this.finish();
            }
        });


        Intent intent = getIntent();
        int num01 = intent.getIntExtra("num01", 0);
        int num02 = intent.getIntExtra("num02", 0);

        int sum = num01 - num02;
        mTextViewResult.setText("" + sum);

        intent = new Intent();
        intent.putExtra("result", sum);
        setResult(Activity.RESULT_OK, intent);
    }
}
