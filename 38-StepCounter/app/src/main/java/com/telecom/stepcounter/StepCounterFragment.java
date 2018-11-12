package com.telecom.stepcounter;


import android.content.Context;
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
import android.widget.TextView;

public class StepCounterFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "StepCounterFragment";
    int detectorCount = 0;
    private SensorManager mSensorManager;
    private Sensor mStepCountSensor;
    private Sensor mDetectorSensor;
    private Sensor mOrientationSensor;
    private TextView mStepCountTextView;
    private TextView mDetectorCountTextView;

    public static StepCounterFragment newInstance() {
        return new StepCounterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ALL))
            System.out.println(sensor.getType());
        mStepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step_counter, container, false);
        mStepCountTextView = v.findViewById(R.id.step_count_text_view);
        mDetectorCountTextView = v.findViewById(R.id.detector_count_text_view);
        //注册 步数计数器
        mSensorManager.registerListener(this, mStepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //注册 步进检测器
        mSensorManager.registerListener(this, mDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_FASTEST);

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
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            System.out.println(Float.toString(event.values[0]));
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
