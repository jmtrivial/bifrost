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

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticRule {

    private List<SemanticColorFilter> semanticFilters;
    private List<SemanticElement> semanticElements;

    private SemanticRule(List<SemanticColorFilter> filters, List<SemanticElement> elems) {
        semanticFilters = filters;
        semanticElements = elems;
    }
    public static SemanticRule parse(XmlResourceParser parser) throws Exception {
        if (!parser.getName().equals("rule"))
            return null;
        List<SemanticElement> elems = new ArrayList<>();
        List<SemanticColorFilter> filters = new ArrayList<>();

        String hue = parser.getAttributeValue(null, "hue");
        String saturation = parser.getAttributeValue(null, "saturation");
        String contrast = parser.getAttributeValue(null, "lightness");
        if (hue != null && saturation != null && contrast != null)
            filters.add(new SemanticColorFilter(hue, saturation, contrast));

        parser.next();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("name-element")) {
                    String n = parser.getAttributeValue(null, "name");
                    if (n != null)
                        elems.add(new SemanticElement(n));
                }
                else if (parser.getName().equals("match")) {
                    hue = parser.getAttributeValue(null, "hue");
                    saturation = parser.getAttributeValue(null, "saturation");
                    contrast = parser.getAttributeValue(null, "lightness");
                    if (hue != null && saturation != null && contrast != null)
                        filters.add(new SemanticColorFilter(hue, saturation, contrast));

                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("rule")) {
                    assert(!filters.isEmpty());
                    return new SemanticRule(filters, elems);
                }
            }
            eventType = parser.next();
        }
        return null;
    }

    boolean accept(FuzzyColor fc) {
        for(SemanticColorFilter filter: semanticFilters)
                if (filter.match(fc))
                    return true;
        assert(!semanticFilters.isEmpty());
        return false;
    }

    SemanticColor toSemanticColor(FuzzyColor realColor) {
        return new SemanticColor(semanticElements, realColor);
    }
}
