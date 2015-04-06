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

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import info.jmfavreau.bifrost.R;

import static java.sql.DriverManager.println;

/**
 * Created by Jean-Marie Favreau on 22/02/15.
 */
public class FuzzyColorRules {
    private List<FuzzyRule> hRules;
    private List<FuzzyRule> sRules;
    private List<FuzzyRule> lRules;

    private static FuzzyColorRules instance = null;

    private FuzzyColorRules(Activity activity) throws XmlPullParserException, IOException {
        hRules = new ArrayList<>();
        sRules = new ArrayList<>();
        lRules = new ArrayList<>();
        loadRules(activity);
    }

    private void loadRules(Activity activity) throws XmlPullParserException, IOException {
        List<String> dim = Arrays.asList("hue", "saturation", "lightness");
        String current = "";
        Resources res = activity.getResources();
        XmlResourceParser parser = res.getXml(R.xml.colors);
        parser.next();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                // we are inside a known dimension (hue, saturation or lightness
                if (!current.isEmpty() && parser.getName().equals("fuzzy-label")) {
                    loadRule(parser, current);
                }
                if (dim.contains(parser.getName())) {
                    current = parser.getName();
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (dim.contains(parser.getName()))
                    current = "";
            }
            eventType = parser.next();
        }
    }

    private void loadRule(XmlResourceParser parser, String current) throws IOException, XmlPullParserException {
        assert parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("fuzzy-label");
        String name = parser.getAttributeValue(null, "name");
        int eventType = parser.next();
        while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("fuzzy-label"))) {
            if (name != null && eventType == XmlPullParser.START_TAG && parser.getName().equals("fuzzy-range")) {
                double max0 = parser.getAttributeFloatValue(null, "max0", -1);
                double max1 = parser.getAttributeFloatValue(null, "max1", -1);
                double min0 = parser.getAttributeFloatValue(null, "min0", -1);
                double min1 = parser.getAttributeFloatValue(null, "min1", -1);
                if (max0 >= 0 && max1 >= 0 && min0 >= 0 && min1 >= 0)
                    addRule(current, name, min0, min1, max1, max0);
            }
            eventType = parser.next();
        }
    }

    private void addRule(String current, String name, double min0, double min1, double max1, double max0) {
        if (current.equals("hue"))
            hRules.add(new FuzzyRule(min0, min1, max1, max0, name));
        else if (current.equals("saturation"))
            sRules.add(new FuzzyRule(min0, min1, max1, max0, name));
        else {
            assert current.equals("lightness");
            lRules.add(new FuzzyRule(min0, min1, max1, max0, name));
        }
    }


    public static void load(Activity activity) {
        if (instance == null) {
            try {
                instance = new FuzzyColorRules(activity);
            }
            catch (IOException e){
            }
            catch (XmlPullParserException e) {
            }
            finally {
            }
        }
    }

    public static FuzzyColorRules getInstance() throws Resources.NotFoundException {
        if (instance == null) {
            throw new Resources.NotFoundException();
        }
        else
            return instance;
    }

    public List<FuzzyColorElement> getFuzzyDescriptionHue(double h) {
        return getFuzzyDescription(h, hRules);
    }

    public List<FuzzyColorElement> getFuzzyDescriptionSaturation(double s) {
        return getFuzzyDescription(s, sRules);
    }

    public List<FuzzyColorElement> getFuzzyDescriptionLightness(double l) {
        return getFuzzyDescription(l, lRules);
    }

    private List<FuzzyColorElement> getFuzzyDescription(double v, List<FuzzyRule> rules) {
        List<FuzzyColorElement> result = new ArrayList<>();
        Iterator i = rules.iterator();
        while(i.hasNext()) {
            FuzzyRule r = (FuzzyRule)i.next();
            double fv = r.getMembershipValue(v);
            if (fv != 0)
                result.add(new FuzzyColorElement(r.getName(), fv));
        }
        return result;
    }
}
