package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Accelerometers extends AppCompatActivity implements SensorEventListener {
    // Code is taken from the following guide unless explicitly stated otherwise: https://www.youtube.com/watch?v=LsWJipo4knk

    private TextView textView;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometers);

        textView = findViewById(R.id.text_accelerometer);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // I replaced MainActivity with Accelerometer as the code is not located in MainActivity.
        sensorManager.registerListener(Accelerometers.this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Logic is the same as in the guide but I rewrote it.
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        textView.setText("x-axis: " + x + "\n" + "y-axis: " + y + "\n" + "z-axis: " + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not in use.
    }
}