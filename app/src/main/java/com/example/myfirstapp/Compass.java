package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

//skip the import for android.app.Activity;

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

import org.w3c.dom.Text;

public class Compass extends AppCompatActivity { //implements SensorEventListener{

    //Försök 1: Utgår från denna guiden: https://www.javacodegeeks.com/2013/09/android-compass-code-example.html
    //Den verkar dock inte fungera helt, så kommenterar ut koden istället. Provar en annan guide. https://www.youtube.com/watch?v=CXS5upB7yvw
    //de 5 första minuterna är de viktigaste att titta på typ

    float[] mGravityValues = new float[3];
    private float[] mAccelerometerValues = new float[3];
    private float[] mRotationMatrix = new float[9];
    private float mLastDirectionInDegrees = 0f;

    //eget tillägg, ej i guiden ännu
    private ImageView mImageViewCompass;
    private SensorManager mSensorManager;
    private Sensor mMagnetometer;
    private Sensor mAccelerometer;
    private TextView viewDegrees;

    private SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            calculateCompassDirection(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Not in use
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

        //eget tillägg
        viewDegrees = (TextView) findViewById(R.id.viewDegrees);
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
            // I'm renaming azimuth to deg, such a weird word...
            float deg = (float) Math.toDegrees(-orientationValues[0]);

            RotateAnimation rotateAnimation = new RotateAnimation(
                    mLastDirectionInDegrees,
                    deg,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );

            rotateAnimation.setDuration(50);
            rotateAnimation.setFillAfter(true);
            mImageViewCompass.startAnimation(rotateAnimation);

            mLastDirectionInDegrees = deg;

            //eget tillägg: visa nuvarande antal grader
            viewDegrees.setText("Last direction: " + mLastDirectionInDegrees + " degrees.");
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

    /*
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    TextView tvHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        //kod från guiden
        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Note that sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) is deprecated nowadays
        // Källa här: https://source.android.com/devices/sensors/sensor-types#orientation_deprecated
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME );
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //Problem att sensorn inte verkar uppdatera, hur göra...? hmmm
        float degree = Math.round(sensorEvent.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees.");

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        ra.setDuration(210);
        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not used
    }
     */
}