package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Code is from this tutorial: https://www.youtube.com/watch?v=2ueIhlDj2cs
        Button accButton = findViewById(R.id.accButton);

        // I skipped the step at ca 1.04 because it prevented the gradle build.
        accButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Accelerometers.class);
            startActivity(intent);
        });

        // Replicated code for navigation between the main menu and compass activity.
        Button compassButton = findViewById(R.id.compassButton);

        compassButton.setOnClickListener( u -> {
            Intent intent = new Intent(this, Compass.class);
            startActivity(intent);
        });
    }
}