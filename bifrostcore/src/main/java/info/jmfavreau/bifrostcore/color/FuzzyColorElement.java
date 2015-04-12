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

import java.util.List;

/**
 * Created by jm on 22/02/15.
 */
public class FuzzyColorElement {

    private String name;
    private double value;

    public double getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    public FuzzyColorElement(String name, double value) {
        if (value < 0.)
            this.value = 0.;
        else if (value > 1.)
            this.value = 1.;
        else
            this.value = value;
        this.name = name;
    }
    public String toString(Boolean values) {
        if (values)
            return String.format( "%s (%.2f)", name, value);
        else
            return String.format( "%s", name);
    }

    public static FuzzyColorElement getMainComponent(List<FuzzyColorElement> list) {
        double v = -1;
        FuzzyColorElement result = null;
        for(FuzzyColorElement e: list)
            if (e.getValue() > v) {
                v = e.getValue();
                result = e;
            }
        return result;
    }
}
