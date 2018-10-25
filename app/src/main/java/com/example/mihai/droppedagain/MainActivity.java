package com.example.mihai.droppedagain;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.widget.Toast;

//for testing purpose
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    /*
    25.10.2018
    2 options:
    1) based on calculating the speed, with a simple math formula containing x, y, z, last_x, last_y, last_z
        If the speed is bigger than a threshold, then the event occurs
        !!! Not really applicable
    2) based on changes on all three axis and it is required that there occured a change on all of them
        Need to see what the accelerometers register in order to be able to set a threshold for all of them
        Problems on writing the registered values in a file in order to see the values and set a threshold
     3) how to detect collision on phones
        accelerometer dereglations
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    System.out.println("TestFile X axis: " + Float.toString(x) + "\n");
                    System.out.println("TestFile Y axis: " + Float.toString(y) + "\n");
                    System.out.println("TestFile Z axis: " + Float.toString(z) + "\n");

                    System.out.println("TestFile last X axis: " + Float.toString(last_x) + "\n");
                    System.out.println("TestFile last Y axis: " + Float.toString(last_y) + "\n");
                    System.out.println("TestFile last Z axis: " + Float.toString(last_z) + "\n");
                    //Toast.makeText(this, "shake detected w/ speed: " + x, Toast.LENGTH_SHORT).show();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //Not used because I need them to run in background.
    //25.10.2018 -- Not sure if it is correct like this yet
    /*
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    */

    /*
    //in order to see the values registered and later on for test purpose(trial and error)
    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(Environment.getExternalStorageState());
    }*/
}
