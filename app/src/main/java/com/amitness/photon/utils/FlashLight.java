package com.amitness.photon.utils;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

/**
 * Provides a abstraction to the flashlight hardware
 */

@SuppressWarnings("deprecation")
public class FlashLight {
    private Camera.Parameters parameters;
    private Camera camera;
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
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    public void turnOff() {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
    }

    public void release() {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
        camera.release();
    }
}