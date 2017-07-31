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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.amitness.photon.utils.Code;
import com.amitness.photon.utils.FlashLight;

import static java.lang.Thread.sleep;

public class TransmitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressBar progressBar;
    private TextView textView;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private String userMessage;
    private String bitStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);


        // Code for spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.myspinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TransmitActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_items));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(this);
        //Code for drop down ends, onSelect methods at buttom

        // TODO: Create an alert if flashlight is not present in device
        // TODO: Exit the app if no flashlight
        boolean hasFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Log.d("Has Flashlight:", Boolean.toString(hasFlash));
        if (!hasFlash) {
            showNoFlashLightAlert();
        }
    }

    private void showNoFlashLightAlert() {
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
                .setTitle("No command selected!")
                .setMessage("Choose a command.")
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
        if (id == R.id.id_about) {
            //Intent to another activity
            Intent intentAbout = new Intent(TransmitActivity.this, AboutActivity.class);
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
//        EditText edit = (EditText) findViewById(R.id.user_message);
//        userMessage = edit.getText().toString().toUpperCase();
//        userMessage = "a";
        Code code = new Code();
        bitStream = code.getBitStream(userMessage);
        Log.d("User entered:", userMessage);
        if (userMessage.isEmpty()) {
            Log.d("Transmitter", "No command selected.");
            showEmptyMessageAlert();
        } else {
            new Thread() {
                public void run() {
                    transmitData();
                }
            }.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    showProgress();
                }
            }).start();
        }
    }

    private void showProgress() {

        int bitLength = bitStream.length();
        progressStatus = 0;
        while (progressStatus < 100) {
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
            try {
                Thread.sleep(5 * bitLength);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transmitData() {
        final int frequency = 2; // bps
        final int milliSecond = 1000 / frequency;
        FlashLight led = new FlashLight();
        try {
            for (char bit : bitStream.toCharArray()) {
                if (bit == '1') {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] codes = {"A", "B", "C", "D", "E", "F", "G"};
        Log.d("nice", String.valueOf(position));
        userMessage = codes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        userMessage = "";

    }
}