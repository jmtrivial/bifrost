package info.jmfavreau.bifrost.color;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticRule {
    private SemanticFilter hueFilter;
    private SemanticFilter saturationFilter;
    private SemanticFilter lightnessFilter;

    private List<SemanticElement> semanticElements;

    private SemanticRule(String h, String s, String l, List<String> elements) {
        hueFilter = new SemanticFilter(h);
        saturationFilter = new SemanticFilter(s);
        lightnessFilter = new SemanticFilter(l);
        semanticElements = new ArrayList<>();
        for(String e: elements)
            semanticElements.add(new SemanticElement(e));

    }
    public static SemanticRule parse(XmlResourceParser parser) throws XmlPullParserException, IOException {
        if (!parser.getName().equals("rule"))
            return null;
        String hue = parser.getAttributeValue(null, "hue");
        String saturation = parser.getAttributeValue(null, "saturation");
        String contrast = parser.getAttributeValue(null, "contrast");
        List<String> elems = new ArrayList<>();

        parser.next();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("name-element")) {
                    String n = parser.getAttributeValue(null, "name");
                    if (n != null)
                        elems.add(n);
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("rule"))
                    return new SemanticRule(hue, contrast, saturation, elems);
            }
            eventType = parser.next();
        }
        return null;
    }

    Boolean accept(FuzzyColor fc) {
        // TODO
        return true;
    }
}
