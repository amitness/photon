package com.amitness.photon;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

public class ReceiveActivity extends AppCompatActivity {

    private TextView mTextViewLightLabel;
    private SensorManager mSensorManager;
    private SensorEventListener mEventListenerLight;
    private float lastLightValue;
    private float bgValue = -1;
    private ArrayList<Float> values = new ArrayList<>();
    private TreeMap<Long, Float> records;
    private long startTime;
    private long referenceTime;
    private long lastTime;

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                char bit;
                if(bgValue == -1){
                    bgValue = lastLightValue;
                    Log.d("Background Intensity", String.valueOf(bgValue));
                }
                if(lastLightValue > bgValue) {
                    bit = '1';
                }
                else {
                    bit = '0';
                }
                mTextViewLightLabel.append(""+bit);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        records = new TreeMap<Long, Float>();

        mTextViewLightLabel = (TextView) findViewById(R.id.sensorValue);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mEventListenerLight = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(bgValue == -1){
                  //startTime = System.currentTimeMillis();
                    //startTime = event.timestamp;
                    startTime = System.currentTimeMillis();
                    lastTime = System.currentTimeMillis();
                    referenceTime = System.currentTimeMillis();
                    Log.d("Start timestamp: ", String.valueOf(startTime));
                    Log.d("Start timestamp: ", String.valueOf(referenceTime));
                    records.put(0L, event.values[0]);
                }
                lastLightValue = event.values[0];
                //long timestamp = event.timestamp;
                long timestamp = System.currentTimeMillis();
                if((timestamp - lastTime) > 999) {
                    Log.d("1 second.", "passed.");
                    lastTime = timestamp;
                    records.put(timestamp - startTime, lastLightValue);
                }
                //records.put( referenceTime + Math.round((timestamp - startTime) / 1000000.0), lastLightValue);
                //records.put(timestamp - startTime, lastLightValue);
                Log.d("Time Stamp:", String.valueOf(timestamp));
                values.add(lastLightValue);
                Log.d("Sensor Value", String.valueOf(lastLightValue));
                updateUI();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mEventListenerLight, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL);
        //mSensorManager.registerListener(mEventListenerLight, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), 1000000000);
    }

    @Override
    public void onStop() {
        Log.d("Read all values:", String.valueOf(values));
        Log.d("Read all values:", String.valueOf(records));
        mSensorManager.unregisterListener(mEventListenerLight);
        super.onStop();
    }

    public void notifyStart(View v) {
        Log.d("startReceive", "Receive Started");
    }
}