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

package info.jmfavreau.bifrost.processing;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import info.jmfavreau.bifrost.speech.SpeechEngine;

/**
 * Created by Jean-Marie Favreau on 15/04/15.
 */
public class CameraToSpeech {
    SpeechEngine speechEngine = null;
    private Camera camera;

    public CameraToSpeech(Context context) {
        speechEngine = new SpeechEngine(context);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            processImage(data, camera);
            camera.startPreview();
        }
    };

    private void processImage(byte[] data, Camera camera) {
        Log.d("bifrost", "camera to speech");
        // extract a series of colors from the image
        // TODO

        // convert colors into semantic colors
        // TODO

        // speak
        // TODO
    }


    public void run() {
        camera.takePicture(null, mPicture, null, null);
        // TODO: restart surface ?
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
