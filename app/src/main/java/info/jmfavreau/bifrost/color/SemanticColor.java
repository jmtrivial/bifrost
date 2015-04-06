package info.jmfavreau.bifrost.color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean-Marie Favreau on 05/04/15.
 */
public class SemanticColor {
    private List<String> elements = null;

    private SemanticColor() {
    }
    public SemanticColor(List<SemanticElement> semanticElements, FuzzyColor realColor) {
        elements = new ArrayList<>();
        for(SemanticElement s: semanticElements)
            elements.add(s.consolidate(realColor));
    }

    public static SemanticColor unknownColor() {
        return new SemanticColor();
    }

    public static String getUnknownName() {
        return "unknown";
    }

    public String toString() {
        if (elements == null || elements.isEmpty())
            return getUnknownName();
        else {
            String result = null;
            for(String e: elements) {
                if (result == null)
                    result = new String();
                else
                    result += " ";
                result += e;
            }
            return result;
        }
    }
}
