package com.telecom.stepcounterfinal;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StepCounterFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "StepCounterFragment";
    int detectorCount = 0;
    private SensorManager mSensorManager;
    private Sensor mStepCountSensor;
    private Sensor mStepDetectorSensor;
    private Sensor mOrientationSensor;
    private TextView mStepCountTextView;
    private TextView mDetectorCountTextView;
    private TextView mOrientationAngleTextView;
    private ImageView mCompassImageView;
    private Button mOrientationButton;

    public static StepCounterFragment newInstance() {
        return new StepCounterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ALL))
            System.out.println(sensor.getType());
        /*
        TYPE_STEP_COUNTER 用户每迈出一步，此传感器就会触发一个事件。
        对于每个用户步伐，此传感器提供一个返回值为 1.0 的事件和一个指示此步伐发生时间的时间戳。
        当用户在行走时，会产生一个加速度上的变化，从而出触发此传感器事件的发生。
        注意此传感器只能检测到单个有效的步伐，获取单个步伐的有效数据，如果需要统计一段时间内的步伐总数，
        则需要使用下面的TYPE_STEP_COUNTER传感器。
         */
        mStepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        /*
        TYPE_STEP_DETECTOR 此传感器会针对检测到的每个步伐触发一个事件，但提供的步数是自设备启动激活该传感器以来累计的总步数，
        在每次设备重启后会清零，所以务必需要做数据的持久化。该传感器返回一个float的值，100步即100.0，以此类推。
        该传感器也有一个时间戳成员，记录最后一个步伐的发生事件。该传感器是需要硬件支持的，并且是非常省电的，
        如果需要长时间获取步伐总数，就不需要解注册该传感器，注册该传感器会一直在后台运行计步。
        请务必在应用程序中保持注册该传感器，否则该传感器不会被激活从而不会统计总部署。
         */
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step_counter, container, false);
        mStepCountTextView = v.findViewById(R.id.step_count_text_view);
        mDetectorCountTextView = v.findViewById(R.id.detector_count_text_view);
        mOrientationAngleTextView = v.findViewById(R.id.orientation_angle_text_view);
        mCompassImageView = v.findViewById(R.id.compass_image);


        //注册 步数计数器
        mSensorManager.registerListener(this, mStepCountSensor, SensorManager.SENSOR_DELAY_UI);
        //注册 步进检测器
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
        mOrientationButton = v.findViewById(R.id.orientation_button);
        mOrientationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), OrientationActivity.class));
            }
        });
        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.i(TAG, event.sensor.getName() + " onSensorChanged");
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
            mStepCountTextView.setText(Float.toString(event.values[0]));
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR && event.values[0] == 1.0) {
            detectorCount++;
            mDetectorCountTextView.setText(Integer.toString(detectorCount));
        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            int angle = (int) event.values[0];
            angle -=8;//沈阳磁偏角大约8度
            if(angle<0)
                angle+=360;

            mOrientationAngleTextView.setText(Float.toString(angle));
            mCompassImageView.setRotation(-event.values[0]);
        }
        /*if(event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            System.out.println("-------------");
            System.out.println(event.values[0]);
            System.out.println(event.values[1]);
            System.out.println(event.values[2]);
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Log.i(TAG, sensor.getName() + " onAccuracyChanged");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
