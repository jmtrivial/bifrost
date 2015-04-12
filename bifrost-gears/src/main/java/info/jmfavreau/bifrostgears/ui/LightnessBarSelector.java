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

package info.jmfavreau.bifrostgears.ui;

import android.content.Context;
import android.util.AttributeSet;

import info.jmfavreau.bifrostcore.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 15/03/15.
 */
public class LightnessBarSelector extends ColorBarSelector {
    public LightnessBarSelector(Context context) {
        super(context);
    }

    public LightnessBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightnessBarSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected double getValueFromHSLColor() {
        return color.getLightness();
    }

    protected void setColorFromValue(double c) {
        color.setLightness(c);
    }

    protected int updateColorFromCursorValue(HSLColor c, double v) {
        c.setLightness(v);
        return c.getIntColor();
    }


}
