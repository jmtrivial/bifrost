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

/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticElement {
    private boolean isTranslation;
    private String value;
    public SemanticElement(String str) {
        if (str.startsWith("tr(")) {
            isTranslation = true;
            value = str.replaceAll("tr\\((.+)\\)", "$1");
        }
        else {
            isTranslation = false;
            value = str;
        }
    }


    public String consolidate(FuzzyColor color) {
        if (isTranslation) {
            FuzzyColorElement comp = null;
            if (value.equals("hue"))
                comp = color.getMainHueComponent();
            else if (value.equals("saturation"))
                comp = color.getMainSaturationComponent();
            else if (value.equals("lightness"))
                comp = color.getMainLightnessComponent();
            else
                return SemanticColorRules.translate(value);

            if (comp == null)
                return SemanticColor.getUnknownName();
            else {
                return SemanticColorRules.translate(comp.getName());
            }
        }
        else
            return value;
    }
}
