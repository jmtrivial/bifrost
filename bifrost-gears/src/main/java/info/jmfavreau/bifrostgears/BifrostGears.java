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

package info.jmfavreau.bifrostgears;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;


import java.util.Locale;

import info.jmfavreau.bifrostcore.color.FuzzyColorRules;
import info.jmfavreau.bifrostcore.color.SemanticColor;
import info.jmfavreau.bifrostcore.color.SemanticColorRules;
import info.jmfavreau.bifrostgears.ui.HSLColorPicker;


public class BifrostGears extends FragmentActivity {
    TextToSpeech textToSpeech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuzzyColorRules.load(this);
        SemanticColorRules.load(this, "standard");
        setContentView(R.layout.activity_bifrostgears);

        Button button= (Button) findViewById(R.id.speech_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoIt(v);
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            textToSpeech.setLanguage(Locale.getDefault());
                        }
                    }
                });

    }

    private void DoIt(View v) {
        HSLColorPicker cp = (HSLColorPicker) getFragmentManager().findFragmentById(R.id.hsl_color_picker);
        SemanticColor c = cp.getSemanticColor();
        if (Build.VERSION.RELEASE.startsWith("5")) {
            textToSpeech.speak(c.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(c.toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
