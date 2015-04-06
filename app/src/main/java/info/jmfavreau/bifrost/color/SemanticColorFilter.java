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

package info.jmfavreau.bifrost.color;


/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticColorFilter {
    private SemanticFilter hueFilter;
    private SemanticFilter saturationFilter;
    private SemanticFilter lightnessFilter;

    public SemanticColorFilter(String hue,
                               String saturation,
                               String lightness) throws Exception {
        hueFilter = new SemanticFilter(hue);
        saturationFilter = new SemanticFilter(saturation);
        lightnessFilter = new SemanticFilter(lightness);
    }

    Boolean match(FuzzyColor color) {
        boolean r = hueFilter.match(color.getHueComponent()) &&
                saturationFilter.match(color.getSaturationComponent()) &&
                lightnessFilter.match(color.getLightnessComponent());
        return r;
    }
}
