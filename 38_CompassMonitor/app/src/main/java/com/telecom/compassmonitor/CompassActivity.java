package com.telecom.compassmonitor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {


    //Allocate data arrays once and reuse
    float[] temp = new float[9];
    float[] rotation = new float[9];
    float[] values = new float[3];
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mField;
    private TextView valueView, directionView;
    private float[] mGravity = new float[3];
    private float[] mMagnetic = new float[3];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获取加速度传感器
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //获取磁场传感器
        mField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        valueView = (TextView) findViewById(R.id.values);
        directionView = (TextView) findViewById(R.id.direction);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mField, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void updateDirection() {

        //Load rotation matrix into R
        //R[] 是一个旋转矩阵，用来保存磁场和加速度的数据
        //第一个就是我们需要填充的R数组，大小是9
        //第二个是是一个转换矩阵，将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
        //第三个是一个大小为3的数组，表示从加速度感应器获取来的数据  在onSensorChanged中
        //第四个是一个大小为3的数组，表示从磁场感应器获取来的数据    在onSensorChanged中
        SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);
        //Remap to camera's point-of-view
        //SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Y, rotation);
        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotation);
        //Return the orientation values
        SensorManager.getOrientation(rotation, values);
        //Convert to degrees
        for (int i = 0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }
        //Display the compass direction
        directionView.setText(getDirectionFromDegrees(values[0]));
        //Display the raw values
        valueView.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f",
                values[0], values[1], values[2]));
    }

    private String getDirectionFromDegrees(float degrees) {
        if (degrees >= -15 && degrees < 15) {
            return "正北";
        } else if (degrees >= 15 && degrees < 45) {
            return "北偏东";
        } else if (degrees >= 45 && degrees < 75) {
            return "东偏北";
        } else if (degrees >= 75 && degrees < 105) {
            return "正东";
        } else if (degrees >= 105 && degrees < 135) {
            return "东偏南";
        } else if (degrees >= 135 && degrees < 165) {
            return "南偏东";
        } else if (degrees >= 165 || degrees < -165) {
            return "正南";
        } else if (degrees >= -165 && degrees < -135) {
            return "南偏西";
        } else if (degrees >= -135 && degrees < -105) {
            return "西偏南";
        } else if (degrees >= -105 && degrees < -75) {
            return "正西";
        } else if (degrees >= -75 && degrees < -45) {
            return "西偏北";
        } else if (degrees >= -45 && degrees < -15) {
            return "北偏西";
        }
        /*if(degrees >= -22.5 && degrees < 22.5) { return "N"; }
        if(degrees >= 22.5 && degrees < 67.5) { return "NE"; }
        if(degrees >= 67.5 && degrees < 112.5) { return "E"; }
        if(degrees >= 112.5 && degrees < 157.5) { return "SE"; }
        if(degrees >= 157.5 || degrees < -157.5) { return "S"; }
        if(degrees >= -157.5 && degrees < -112.5) { return "SW"; }
        if(degrees >= -112.5 && degrees < -67.5) { return "W"; }
        if(degrees >= -67.5 && degrees < -22.5) { return "NW"; }*/

        return null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        //Copy the latest values into the correct array
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mGravity, 0, event.values.length);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mMagnetic, 0, event.values.length);
                break;
            default:
                return;
        }

        if (mGravity != null && mMagnetic != null) {
            updateDirection();
        }
    }

}
