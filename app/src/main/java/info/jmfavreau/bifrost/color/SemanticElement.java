package info.jmfavreau.bifrost.color;

import android.util.Log;

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
                Log.w("traduction", comp.getName() + " donne " + SemanticColorRules.translate(comp.getName()));
                return SemanticColorRules.translate(comp.getName());
            }
        }
        else
            return value;
    }
}
