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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;

import junit.framework.Assert;

import org.opencv.core.Scalar;

import info.jmfavreau.bifrost.speech.SpeechEngine;
import info.jmfavreau.bifrostcore.color.FuzzyColor;
import info.jmfavreau.bifrostcore.color.SemanticColor;
import info.jmfavreau.bifrostcore.imageprocessing.ImageToColor;

/**
 * Created by Jean-Marie Favreau on 15/04/15.
 */
public class CameraToSpeech {
    SpeechEngine speechEngine = null;
    ImageToColor imageToColor = null;
    private Camera camera;

    public CameraToSpeech(Context context) {
        speechEngine = new SpeechEngine(context);
        imageToColor = new ImageToColor();
    }

    private Camera.PictureCallback mPictureJPG = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Assert.assertNotNull(data);
            Assert.assertTrue(data.length != 0);
            processImage(data, camera);
            camera.startPreview();
        }
    };

    private void processImage(byte[] data, Camera camera) {
        Assert.assertNotNull(data);
        Assert.assertTrue(data.length != 0);
        Log.d("bifrost", "camera to speech");

        // convert the raw data as a bitmap
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tmp = BitmapFactory.decodeByteArray(data, 0, data.length, op);

        // extract a series of colors from the image
        Scalar color = imageToColor.process(tmp);
        Log.d("bifrost", "color: " + color.toString());

        // convert colors into semantic colors
        FuzzyColor fuzzyColor = new FuzzyColor(color);
        SemanticColor semanticColor = new SemanticColor(fuzzyColor);

        // speak
        speechEngine.speak(semanticColor.toString());
    }


    public void run() {
        camera.takePicture(null, null, null, mPictureJPG);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
