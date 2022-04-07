package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Accelerometers extends AppCompatActivity implements SensorEventListener {

    //Ett nästa steg 'imorgon' är att följa denna guiden: https://www.youtube.com/watch?v=LsWJipo4knk

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
        // Fick ange annat klassnamn än i guiden då vi inte befinner oss i MainActivity
        sensorManager.registerListener(Accelerometers.this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Min egen omskrivning, återkomma om vi behöver avrunda typ
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        //sensorn verkar vara väldigt känslig, liksom de sista decimalerna ändras även om telefonen ligger helt stilla

        textView.setText("x-axis: " + x + "\n" + "y-axis: " + y + "\n" + "z-axis: " + z);
        // Nu dags att testa på en riktig telefon, uppdateras värdena?
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //TODO
    }
}