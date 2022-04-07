package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

//The code is mainly taken from this guide: https://www.youtube.com/watch?v=CXS5upB7yvw
//I skipped the import for android.app.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass extends AppCompatActivity {

    float[] mGravityValues = new float[3];
    private float[] mAccelerometerValues = new float[3];
    private float[] mRotationMatrix = new float[9];
    private float mLastDirectionInDegrees = 0f;

    //I added the following attributes, not shown in the guide.
    private ImageView mImageViewCompass;
    private SensorManager mSensorManager;
    private Sensor mMagnetometer;
    private Sensor mAccelerometer;
    private TextView viewDegrees;

    //Added for testing filtering sensor data
    private ExponentialMovingAverage ema;

    private SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            calculateCompassDirection(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Not in use.
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        mImageViewCompass = (ImageView) findViewById(R.id.imageViewCompass);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //My own code addition.
        viewDegrees = (TextView) findViewById(R.id.viewDegrees);
        ema = new ExponentialMovingAverage(0.125);
    }

    private void calculateCompassDirection(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGravityValues = event.values.clone();
                break;
        }
        boolean success = SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerValues, mGravityValues);
        if(success) {
            float[] orientationValues = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientationValues);
            // I renamed azimuth to deg.
            float deg = (float) Math.toDegrees(-orientationValues[0]);

            //Own addition to make the compass less shaky
            float avgDeg = (float) ema.average(deg);

            RotateAnimation rotateAnimation = new RotateAnimation(
                    mLastDirectionInDegrees,
                    avgDeg,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );

            rotateAnimation.setDuration(100); //Increased from 50 to 100
            rotateAnimation.setFillAfter(true);
            mImageViewCompass.startAnimation(rotateAnimation);

            mLastDirectionInDegrees = (float) avgDeg;

            //Own addition to show what the current degree is.
            double showDeg = Math.round(mLastDirectionInDegrees);
            viewDegrees.setText("Last direction: " + showDeg + " degrees.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mMagnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    // The following code is from here: https://stackoverflow.com/questions/9200874/implementing-exponential-moving-average-in-java/9201081#9201081
    private class ExponentialMovingAverage {
        private double alpha;
        private Double oldValue;

        public ExponentialMovingAverage(double alpha) {
            this.alpha = alpha;
        }

        public double average(double value) {
            if (oldValue == null) {
                oldValue = value;
                return value;
            }
            double newValue = oldValue + alpha * (value - oldValue);
            oldValue = newValue;
            return newValue;
        }
    }

}