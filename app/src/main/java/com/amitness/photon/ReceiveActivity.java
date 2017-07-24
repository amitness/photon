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

import com.amitness.photon.utils.BaudotCode;

import java.util.ArrayList;
import java.util.TreeMap;

public class ReceiveActivity extends AppCompatActivity {

    private TextView mTextViewLightLabel;
    private SensorManager mSensorManager;
    private SensorEventListener mEventListenerLight;
    private float currentLightIntensity;
    private float bgIntensity = -1;
    private ArrayList<Float> intensityValues = new ArrayList<>();
    private TreeMap<Long, Float> records;
    private long startTime;
    private long lastTime;
    private String bit;
    private String rawReading = "";
    private boolean started = false;
    private String lastFiveBits;
    private String payload = "";
    private boolean startBitDetected = false;
    private boolean isTransferring = true;

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mTextViewLightLabel.append(bit);
                BaudotCode bc = new BaudotCode();
                String message = bc.decode(payload);
                mTextViewLightLabel.setText(message);
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
//        mTextViewLightLabel.setText("Receiving...");

        mEventListenerLight = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (bgIntensity == -1) {
                    //startTime = System.currentTimeMillis();
                    //startTime = event.timestamp;
                    startTime = System.currentTimeMillis();
//                    referenceTime = System.currentTimeMillis();
                    Log.d("Start timestamp: ", String.valueOf(startTime));
//                    Log.d("Start timestamp: ", String.valueOf(referenceTime));
                    bgIntensity = event.values[0];
                    records.put(0L, bgIntensity);
                    Log.d("Background Intensity: ", String.valueOf(bgIntensity));
//                    rawReading += "0";
                }

                Log.d("RawReading:", String.valueOf(rawReading));
                currentLightIntensity = event.values[0];
                if (currentLightIntensity > 1000 && !started) {
                    lastTime = System.currentTimeMillis();
                    started = true;
//                    mTextViewLightLabel.setText("1");
                    rawReading += "1";
                }
                //long timestamp = event.timestamp;
                if (currentLightIntensity > bgIntensity) {
                    bit = "1";
                } else {
                    bit = "0";
                }
                long currentTime = System.currentTimeMillis();
                if ((currentTime - lastTime) > 999 && started) {
                    Log.d("1 second.", "passed.");
                    lastTime = currentTime;
                    records.put(currentTime - startTime, currentLightIntensity);
                    Log.d("Bit:", bit);
//                    mTextViewLightLabel.setText(bit);
                    rawReading += bit;
                }


                //records.put( referenceTime + Math.round((timestamp - startTime) / 1000000.0), lastLightValue);
                //records.put(timestamp - startTime, lastLightValue);
//                Log.d("Time Stamp:", String.valueOf(timestamp));
                intensityValues.add(currentLightIntensity);
//                Log.d("Sensor Value", String.valueOf(lastLightIntensity));


                String startBits = "11111";
                String stopBits = "00000";


                if (rawReading.length() >= 5) {
                    lastFiveBits = rawReading.substring(rawReading.length() - 5);
                    if (!startBitDetected) {
                        if (lastFiveBits.equals(startBits)) {
                            System.out.println("Start bit detected.");
                            mTextViewLightLabel.setText("Start bit detected.");
                            startBitDetected = true;
                            // received =  "";
                            // System.exit(0);
                        }
                    } else {

                        if (!lastFiveBits.equals(stopBits)) {
                            payload += lastFiveBits;
                        } else {
                            System.out.println("Stop bit detected.");
                            isTransferring = false;
//                            mSensorManager.unregisterListener(mEventListenerLight);
//                            updateUI();
                            mSensorManager.unregisterListener(mEventListenerLight);
                            updateUI();
                        }
                    }
                    rawReading = "";
                }
                // System.out.println("==>" + received);

//                updateUI();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mEventListenerLight, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        Log.d("Read all intensities:", String.valueOf(intensityValues));
        Log.d("Read all values:", String.valueOf(records));
        Log.d("Read all data:", rawReading);
        mSensorManager.unregisterListener(mEventListenerLight);
//        updateUI();
        super.onStop();
    }

    public void notifyStart(View v) {
        Log.d("startReceive", "Receive Started");
    }
}