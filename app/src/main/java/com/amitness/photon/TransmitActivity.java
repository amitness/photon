package com.amitness.photon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amitness.photon.utils.FlashLight;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class TransmitActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private int progressStatus  = 0;
    private Handler handler = new Handler();

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

    private void showEmptyMessageAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Blank Message!")
                .setMessage("Please enter some text to send.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textview);


        Log.d("SendButton", "User clicked the button.");
        EditText edit = (EditText)findViewById(R.id.user_message);
        String userMessage = edit.getText().toString();
        Log.d("User entered:", userMessage);
        if(userMessage.isEmpty()) {
            Log.d("Transmitter", "User message is empty");
            showEmptyMessageAlert();
        } else {
//            new Thread() {
//                public void run() {
//                    transmitData();
//                }
//            }.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    showProgress();
                }
            }).start();
        }
    }

    private void showProgress() {
        String bit = "10101";
        int bitLength = bit.length();
        while(progressStatus < 100) {
            progressStatus += 1;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progressStatus);
                    if (progressStatus < 100) {
                        textView.setText("Progress: " + progressStatus + "/" + progressBar.getMax());
                    } else {
                        textView.setText("Transmission Completed.");
                    }
                }
            });
            try{
                Thread.sleep(10 * bitLength);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void transmitData() {
        int frequency  = 1; // bps
        int milliSecond = 1000 / frequency;
        FlashLight led = new FlashLight();

        HashMap<String, String> morseCode = new HashMap<>();
        morseCode.put("A", ".-");
        String code = morseCode.get("A");
        String message = "10101";
        try {
            for(char bit: message.toCharArray()) {
                if(bit == '1') {
                    led.turnOn();
                } else {
                    led.turnOff();
                }
                sleep(milliSecond);
            }
            led.release();
        } catch (InterruptedException e) {
            String TAG = "Flash";
            Log.w(TAG, "InterruptedException");
        }
    }
}
