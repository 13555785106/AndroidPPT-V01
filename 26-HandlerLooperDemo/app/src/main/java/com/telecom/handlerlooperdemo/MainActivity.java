package com.telecom.handlerlooperdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button mStartButton;
    Button mStopButton;
    Button mCalculateButton;
    RadioGroup mOperatorRadioGroup;
    TextView mMsgTextView;
    TextView mResultTextView;
    CalculateTask mCalculateTask;
    EditText mFirstValueEditText;
    EditText mSecondValueEditText;
    String mOperator = "+";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mStartButton.setEnabled(false);
                    mStopButton.setEnabled(true);
                    mCalculateButton.setEnabled(true);
                    break;
                case 1:
                    if (mResultTextView != null)
                        mResultTextView.setText(msg.getData().getString("result"));
                    break;
                case 2:
                    mStartButton.setEnabled(true);
                    mStopButton.setEnabled(false);
                    mCalculateButton.setEnabled(false);
                    mMsgTextView.setText("后台计算器已经停止");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstValueEditText = (EditText) findViewById(R.id.first_value_edit_text);
        mSecondValueEditText = (EditText) findViewById(R.id.second_value_edit_text);
        mMsgTextView = (TextView) findViewById(R.id.msg_text_view);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mOperatorRadioGroup = (RadioGroup) findViewById(R.id.operator_radio_group);
        mOperatorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.add_radio_button:
                        mOperator = "+";
                        break;
                    case R.id.sub_radio_button:
                        mOperator = "-";
                        break;
                    case R.id.mul_radio_button:
                        mOperator = "*";
                        break;
                    case R.id.div_radio_button:
                        mOperator = "/";
                        break;
                }
            }
        });
        mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalculateTask = new CalculateTask(mHandler);
                Thread thread = new Thread(mCalculateTask);
                thread.start();
                mMsgTextView.setText("后台计算器运行中");
            }
        });
        mStopButton = (Button) findViewById(R.id.button_stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.what = 1;
                mCalculateTask.getHandler().sendMessage(msg);
            }
        });
        mCalculateButton = (Button) findViewById(R.id.calculate_button);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validated = true;
                double firstValue = 0;
                double secondValue = 0;
                try {
                    firstValue = Double.parseDouble(mFirstValueEditText.getText().toString());
                } catch (Exception e) {
                    validated = false;
                    mMsgTextView.setText("第一个数字错误");
                    e.printStackTrace();
                }
                try {
                    secondValue = Double.parseDouble(mSecondValueEditText.getText().toString());
                } catch (Exception e) {
                    validated = false;
                    mMsgTextView.setText("第二个数字错误");
                    e.printStackTrace();
                }

                if(validated) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.getData().putString("operator", mOperator);
                    msg.getData().putDouble("first_value", firstValue);
                    msg.getData().putDouble("second_value", secondValue);
                    mCalculateTask.getHandler().sendMessage(msg);
                }
            }
        });
    }
}
