package com.example.myfirstapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // code is from this tutorial: https://www.youtube.com/watch?v=2ueIhlDj2cs
        Button accButton = findViewById(R.id.accButton);

        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Accelerometers.class);
            startActivity(intent);
        });
        // skip out on the set stuff in ca 1.04 because it prevented the gradle build

        // code replicated for navigation between main-menu and compass activity
        Button compassButton = findViewById(R.id.compassButton);

        compassButton.setOnClickListener( u -> {
            Intent intent = new Intent(this, Compass.class);
            startActivity(intent);
        });

        //Problem att starta: java.lang.RuntimeException: Canvas: trying to draw too large(538040784bytes) bitmap.
        //Provade att förminska bilden till 10% av dess ursprungliga storlek, funkar det mån tro?


        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
            // This device does not have a compass, turn off the compass feature
            //disableCompassFeature();
            Log.d("compass", "Tested phone does not have a compass.");
        }

    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}