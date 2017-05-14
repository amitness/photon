package com.amitness.photon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amitness.photon.utils.FlashLight;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class TransmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);

        // TODO: Create an alert if flashlight is not present in device
        // TODO: Exit the app if no flashlight
        boolean hasFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Log.d("Has Flashlight:", Boolean.toString(hasFlash));
        if(!hasFlash) {
            showNoFlashLightAlert();
        }
    }

    private void showNoFlashLightAlert(){
        new AlertDialog.Builder(this)
                .setTitle("No Flashlight!")
                .setMessage("Flashlight is not available on this device.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_about) {
            //Intent to another activity
            Intent intentAbout=new Intent(TransmitActivity.this,AboutActivity.class);
            startActivity(intentAbout);
            return true;
        }
        return true;
    }


    @SuppressWarnings("deprecation")
    public void startTransmission(View view) {
        Log.d("SendButton", "User clicked the button.");

        new Thread() {
            public void run() {
                transmitData();
            }
        }.start();

    }

    private void transmitData() {
        int frequency  = 1; // bps
        int milliSecond = 1000 / frequency;
        FlashLight led = new FlashLight();

        HashMap<String, String> morseCode = new HashMap<>();
        morseCode.put("A", ".-");
        String code = morseCode.get("A");
        String message = "01001001";
        try {
            for(char bit: message.toCharArray()) {
                if(bit == '1') {
                    led.turnOn();
                    Log.d("Camera", "Turned ON");
                } else {
                    led.turnOff();
                    Log.d("Camera", "Turned OFF");
                }
                sleep(milliSecond);
            }
            led.release();
            Log.d("Camera", "Released Sensor");
        } catch (InterruptedException e) {
            String TAG = "Flash";
            Log.w(TAG, "InterruptedException");
        }
    }
}
