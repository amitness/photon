package com.amitness.photon.utils;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;

/**
 * Provides a abstraction to the flashlight hardware
 */

@SuppressWarnings("deprecation")
public class FlashLight {
    private Camera.Parameters parameters;
    private Camera camera;
    public boolean isOn = false;
    // TODO: Implement the methods

    public FlashLight() {
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return;
        }

        SurfaceTexture st;
        st = new SurfaceTexture(0);

        try {
            camera.setPreviewTexture(st);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters = camera.getParameters();

    }

    public void turnOn() {
        if(!isOn) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isOn = true;
            Log.d("FlashLight", "Turned ON");
        }
    }

    public void turnOff() {
        if(isOn) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isOn = false;
            Log.d("FlashLight", "Turned OFF");
        }
    }

    public void release() {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.release();
        Log.d("FlashLight", "Released Sensor");
    }
}