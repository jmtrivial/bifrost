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

import info.jmfavreau.bifrost.speech.SpeechEngine;
import info.jmfavreau.bifrostcore.color.FuzzyColorRules;
import info.jmfavreau.bifrostcore.color.SemanticColorRules;

import android.app.Activity;
import android.os.Bundle;


public class BifrostActivity extends Activity {

    SpeechEngine speechEngine = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuzzyColorRules.load(this);
        SemanticColorRules.load(this, "standard");
        setContentView(R.layout.activity_bifrost);

        speechEngine = new SpeechEngine(getApplicationContext());

    }
}
