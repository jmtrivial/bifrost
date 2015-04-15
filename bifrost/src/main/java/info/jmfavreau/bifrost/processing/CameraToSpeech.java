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

import info.jmfavreau.bifrost.speech.SpeechEngine;

/**
 * Created by Jean-Marie Favreau on 15/04/15.
 */
public class CameraToSpeech {
    SpeechEngine speechEngine = null;

    public CameraToSpeech(Context context) {
        speechEngine = new SpeechEngine(context);
    }


    public void run(byte[] data, Camera camera) {
        // extract a series of colors from the image
        // TODO

        // convert colors into semantic colors
        // TODO

        // speak
        // TODO
    }
}
