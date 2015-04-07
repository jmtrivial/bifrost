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

import android.app.FragmentManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;


import java.util.Locale;

import info.jmfavreau.bifrost.color.FuzzyColorRules;
import info.jmfavreau.bifrost.color.SemanticColor;
import info.jmfavreau.bifrost.color.SemanticColorRules;
import info.jmfavreau.bifrost.ui.HSLColorPicker;

import static info.jmfavreau.bifrost.R.layout.activity_bifrost;


public class Bifrost extends FragmentActivity {
    TextToSpeech ttobj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuzzyColorRules.load(this);
        SemanticColorRules.load(this, "standard");
        setContentView(R.layout.activity_bifrost);

        Button button= (Button) findViewById(R.id.speech_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoIt(v);
            }
        });

        ttobj = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            ttobj.setLanguage(Locale.getDefault());
                        }
                    }
                });

    }

    private void DoIt(View v) {
        HSLColorPicker cp = (HSLColorPicker) getFragmentManager().findFragmentById(R.id.hsl_color_picker);
        SemanticColor c = cp.getSemanticColor();
        ttobj.speak(c.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
