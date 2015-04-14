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

package info.jmfavreau.bifrost.speech;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Jean-Marie Favreau on 14/04/15.
 */
public class SpeechEngine {
    String previousMessage = null;
    long previousTime;

    TextToSpeech ttobj = null;
    private int delay = 5;

    SpeechEngine(Context appContext) {
        ttobj = new TextToSpeech(appContext,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            ttobj.setLanguage(Locale.getDefault());
                        }
                    }
                });
    }

    void replay() {
        if (previousMessage != null) {
            long now =  System.currentTimeMillis();
            if (now - previousTime > delay * 60000) { // n minutes after, it's too late
                previousMessage = null;
            }
            else {
                speak(previousMessage);
            }
        }
    }

    void setDelay(int nbMin) {
        delay = nbMin;
    }

    int getDelay() {
        return delay;
    }

    void speak(String message) {
        previousMessage = message;
        previousTime = System.currentTimeMillis();
        if (ttobj != null)
            ttobj.speak(previousMessage, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
