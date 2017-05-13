package com.amitness.photon;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class TransmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);
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

    @SuppressWarnings("deprecation")
    private void transmitData() {
        int milliSecond = 2000;
        Camera camera;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return;
        }

        SurfaceTexture st = new SurfaceTexture(0);

        try {
            camera.setPreviewTexture(st);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
            Log.d("Camera", "Turned ON");
            sleep(milliSecond);
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            Log.d("Camera", "Turned OFF");
            camera.release();
        } catch (InterruptedException e) {
            String TAG = "Flash";
            Log.w(TAG, "InterruptedException");
        }
    }
}
