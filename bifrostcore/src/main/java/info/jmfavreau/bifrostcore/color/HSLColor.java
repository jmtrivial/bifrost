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

package info.jmfavreau.bifrostcore.color;

import android.graphics.Color;

/**
 * Created by Jean-Marie Favreau on 23/02/15.
 */
public class HSLColor {
    public double getHue() {
        return h;
    }

    public double getSaturation() {
        return s;
    }

    public double getLightness() {
        return l;
    }

    public double getRed() {
        return r;
    }

    public double getGreen() {
        return g;
    }

    public double getBlue() {
        return b;
    }

    public int getIntColor() {
        return Color.argb(255, (int)(255 * r), (int)(255 * g), (int)(255 * b));
    }

    private double h;
    private double s;
    private double l;

    private double r;
    private double g;
    private double b;

    private HSLColor(double h, double s, double l, double r, double g, double b) {
        this.h = h;
        this.s = s;
        this.l = l;
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public HSLColor(HSLColor c)  {
        this.h = c.h;
        this.s = c.s;
        this.l = c.l;
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public static HSLColor fromRGB(double r, double g, double b) {
        HSLColor result = new HSLColor(-1., -1., -1., r, g, b);
        result.initHSLValuesFromRGB(r, g, b);
        return result;
    }

    public static HSLColor fromHSL(double h, double s, double l) {
        HSLColor result = new HSLColor(h, s, l, -1., -1., -1.);
        result.initRGBValuesFromHSL(h, s, l);
        return result;
    }

    public static HSLColor white() {
        return fromRGB(1., 1., 1.);
    }

    public static HSLColor red() {
        return fromRGB(1., 0., 0.);
    }

    /**
     * Initialization of the HSL description
     * @param r red component in [0, 1]
     * @param g green component in [0, 1]
     * @param b blue component in [0, 1]
     */
    private void initHSLValuesFromRGB(double r, double g, double b) {
        double M = Math.max(r, Math.max(g, b));
        double m = Math.min(r, Math.min(g, b));
        this.l = (M - m)/2;


        if (M == m) {
            this.s = this.h = 0.;
        }
        else {
            double c = M - m;
            this.s = this.l > 0.5 ? c / (2 - M - m) : c / (M + m);
            if (M == r) {
                this.h = (g - b) / c + (g < b ? 6 : 0);
            }
            else if (M == g) {
                this.h = (b - r) / c + 2;
            }
            else {
                assert M == b;
                this.h = (r - g) / c + 4;
            }
            this.h /= 6;
        }
    }


    /**
     * Convert a HSLColor to color int
     * @return the int value corresponding to the current HSLColor
     */
    public int toColor() {
        return Color.rgb((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public void setSL(double s, double l) {
        this.s = s;
        this.l = l;
        initRGBValuesFromHSL(h, s, l);
    }

    public void setHue(double h) {
        this.h = h;
        initRGBValuesFromHSL(h, s, l);
    }

    public void setSaturation(double s) {
        this.s = s;
        initRGBValuesFromHSL(h, s, l);
    }

    public void setLightness(double l) {
        this.l = l;
        initRGBValuesFromHSL(h, s, l);
    }

    private void initRGBValuesFromHSL(double h, double s, double l) {
        double c = (1 - Math.abs(2 * l - 1)) * s;
        double hp = h * 6;
        double x = c * (1 - Math.abs(hp % 2  - 1));
        double rgb[] = new double[3];
        for(int i = 0; i != 3; ++i)
            rgb[i] = 0;
        if (0 <= hp && hp < 1) {
            rgb[0] = c;
            rgb[1] = x;
        }
        else if (1 <= hp && hp < 2) {
            rgb[0] = x;
            rgb[1] = c;

        }
        else if (2 <= hp && hp < 3) {
            rgb[1] = c;
            rgb[2] = x;
        }
        else if (3 <= hp && hp < 4) {
            rgb[1] = x;
            rgb[2] = c;
        }
        else if (4 <= hp && hp < 5) {
            rgb[0] = x;
            rgb[2] = c;
        }
        else if (5 <= hp && hp <= 6) {
            rgb[0] = c;
            rgb[2] = x;
        }
        double m = l - 0.5 * c;
        this.r = rgb[0] + m;
        this.g = rgb[1] + m;
        this.b = rgb[2] + m;
    }
}
