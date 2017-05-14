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

public class ReceiveActivity extends AppCompatActivity {

    private TextView mTextViewLightLabel;
    private SensorManager mSensorManager;
    private SensorEventListener mEventListenerLight;
    private float lastLightValue;
    private float bgValue = -1;
    private ArrayList<Float> values = new ArrayList<Float>();

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

        mTextViewLightLabel = (TextView) findViewById(R.id.sensorValue);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mEventListenerLight = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                lastLightValue = event.values[0];
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
    }

    @Override
    public void onStop() {
        Log.d("Read all values:", String.valueOf(values));
        mSensorManager.unregisterListener(mEventListenerLight);
        super.onStop();
    }

    public void notifyStart(View v) {
        Log.d("startReceive", "Receive Started");
    }
}