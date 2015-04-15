/*
 * Bifröst - A camera-to-speech system for Android
 * Copyright (C) 2015 Jean-Marie Favreau <jeanmarie.favreau@free.fr>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package info.jmfavreau.bifrost;

import info.jmfavreau.bifrost.processing.CameraToSpeech;
import info.jmfavreau.bifrost.speech.SpeechEngine;
import info.jmfavreau.bifrost.ui.CameraPreview;
import info.jmfavreau.bifrost.ui.OverlayView;
import info.jmfavreau.bifrostcore.color.FuzzyColorRules;
import info.jmfavreau.bifrostcore.color.SemanticColorRules;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;



public class BifrostActivity extends Activity {

    CameraToSpeech cameraToSpeech = null;
    Camera mCamera = null;
    CameraPreview mPreview;
    OverlayView mView;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            cameraToSpeech.run(data, camera);
            // TODO: send the resulting image to the speech engine
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuzzyColorRules.load(this);
        SemanticColorRules.load(this, "standard");
        setContentView(R.layout.activity_bifrost);

        cameraToSpeech = new CameraToSpeech(getApplicationContext());

        if (checkCameraHardware(this)) {
            // create overlay view
            mView = (OverlayView)this.findViewById(R.id.overlay);
            mView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

            // Create an instance of Camera
            mCamera = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);


        }
        else {
            // TODO: error: no camera available
            Log.w("bifrost", "no camera available");
        }

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
            // Create an instance of Camera
            mCamera = getCameraInstance();
            adjustCameraParameters();
            mPreview.updateCamera(mCamera);
        }
    }

    private void adjustCameraParameters() {
        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();
        // set the focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        // set flash on
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        // set white balance
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_SHADE);
        // set Camera parameters
        mCamera.setParameters(params);
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        try {

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        Log.d("bifrost", "found camera ");
                        return Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e("bifrost", "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return null; // returns null if camera is unavailable
    }


}
