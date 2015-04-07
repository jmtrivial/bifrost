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

package info.jmfavreau.bifrost.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jmfavreau.bifrost.R;
import info.jmfavreau.bifrost.color.FuzzyColor;
import info.jmfavreau.bifrost.color.HSLColor;
import info.jmfavreau.bifrost.color.SemanticColor;
import info.jmfavreau.bifrost.color.SemanticColorRules;

/**
 * Created by Jean-Marie Favreau on 23/02/15.
 */
// https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/HSVColorPickerDialog.java
public class HSLColorPicker extends Fragment implements View.OnClickListener {


    private LightnessBarSelector ls;
    private SaturationBarSelector ss;
    private HueBarSelector hs;
    private ColorViewer cv;
    private EditText tv;

    private HSLColor c;


    public HSLColorPicker() {
        super();

    }


    public void onClick(View v) {
        if (v.getId() == hs.getId()) {
            c = hs.getColor();
            ls.setColor(c);
            ss.setColor(c);
        }
        else if (v.getId() == ss.getId()) {
            c = ss.getColor();
            ls.setColor(c);
            hs.setColor(c);
        }
        else if (v.getId() == ls.getId()) {
            c = ls.getColor();
            ss.setColor(c);
            hs.setColor(c);
        }
        update();
    }

    public void update() {

        ls.redraw();
        ss.redraw();
        hs.redraw();
        cv.setBackgroundColor(c.getIntColor());

        FuzzyColor f = new FuzzyColor(c);
        SemanticColor s = SemanticColorRules.toSemantic(f);
        f.defuzzificationUnary();
        String text = String.format("<small>%s: %.4f, %s: %.4f, %s: %.4f<br />%s: %.2f, %s: %.2f, %s: %.2f<br />%s: %s</small><br /><strong>%s: %s</strong>",
                getString(R.string.hue_label), c.getHue(),
                getString(R.string.saturation_label), c.getSaturation(),
                getString(R.string.lightness_label), c.getLightness(),
                getString(R.string.red_label), c.getRed(),
                getString(R.string.green_label), c.getGreen(),
                getString(R.string.blue_label), c.getBlue(),
                getString(R.string.fuzzy_label), f.toString(true),
                getString(R.string.semantic_label), s.toString());
        tv.setText(Html.fromHtml(text.toString()));
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.hsl_color_picker, container, false);

        c = HSLColor.red();
        hs = (HueBarSelector) view.findViewById(R.id.myHueSelector);
        ss = (SaturationBarSelector) view.findViewById(R.id.mySaturationSelector);
        ls = (LightnessBarSelector) view.findViewById(R.id.myLightnessSelector);
        cv = (ColorViewer) view.findViewById(R.id.myColorViewer);
        tv = (EditText) view.findViewById(R.id.myTextColor);

        ss.setColorChangedListener(this);
        hs.setColorChangedListener(this);
        ls.setColorChangedListener(this);
        ss.setColor(c);
        hs.setColor(c);
        ls.setColor(c);

        update();

        return view;
    }



    public HSLColor getColor() {
        return c;
    }

    public SemanticColor getSemanticColor() {
        FuzzyColor f = new FuzzyColor(c);
        return SemanticColorRules.toSemantic(f);
    }
}
