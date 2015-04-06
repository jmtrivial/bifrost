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
