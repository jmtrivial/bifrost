package info.jmfavreau.bifrost.color;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jm on 22/02/15.
 */
public class FuzzyColor {
    private List<FuzzyColorElement> hue;
    private List<FuzzyColorElement> saturation;
    private List<FuzzyColorElement> lightness;
    private HSLColor color;

    public FuzzyColor(HSLColor c) throws Resources.NotFoundException {
        color = c;
        setFuzzyLabels();
    }

    /**
     * Initialization of a fuzzy color using an RGB description (each component between 0 and 1)
     * @param r red
     * @param g green
     * @param b blue
     */
    static public FuzzyColor fromRGB(double r, double g, double b) throws Resources.NotFoundException {
        return new FuzzyColor(HSLColor.fromRGB(r, g, b));
    }

    /**
     * Initialization of a fuzzy color using an HSL description (each component between 0 and 1)
     * @param h hue
     * @param s saturation
     * @param l lightness
     */
    static public FuzzyColor fromHSL(double h, double s, double l) throws Resources.NotFoundException {
        return new FuzzyColor(HSLColor.fromHSL(h, s, l));
    }

    private void setFuzzyLabels() throws Resources.NotFoundException {
        FuzzyColorRules rules = FuzzyColorRules.getInstance();
        hue = rules.getFuzzyDescriptionHue(color.getHue());
        saturation = rules.getFuzzyDescriptionSaturation(color.getSaturation());
        lightness = rules.getFuzzyDescriptionLightness(color.getLightness());
    }

    /**
     * Defuzzification of this color that generates only one element per component
     */
    public void defuzzificationUnary() {
        hue = defuzzificationUnaryComponent(hue);
        saturation = defuzzificationUnaryComponent(saturation);
        lightness = defuzzificationUnaryComponent(lightness);
    }

    private static List<FuzzyColorElement> defuzzificationUnaryComponent(List<FuzzyColorElement> comp) {
        String name = "";
        double value = 0.;
        ListIterator<FuzzyColorElement> i = comp.listIterator();
        while (i.hasNext()) {
            FuzzyColorElement e = i.next();
            if (e.getValue() > value) {
                name = e.getName();
                value = e.getValue();
            }
        }
        List<FuzzyColorElement> result = new ArrayList<>();
        if (name.compareTo("") != 0)
            result.add(new FuzzyColorElement(name, value));
        return result;
    }

    public String toString() {
        String r = "";
        String h = toStringComponent(hue);
        if (h.compareTo("") != 0);
            r = h;
        String s = toStringComponent(saturation);
        if (s.compareTo("") != 0) {
            if (r.compareTo("") != 0)
                r += ", ";
            r += s;
        }
        String l = toStringComponent(lightness);
        if (l.compareTo("") != 0) {
            if (r.compareTo("") != 0)
                r += ", ";
            r += l;
        }

        if (r.compareTo("") == 0)
            r = "unknown";
        return r;
    }

    private static String toStringComponent(List<FuzzyColorElement> comp) {
        String r = "";
        ListIterator<FuzzyColorElement> i = comp.listIterator();
        while (i.hasNext()) {
            FuzzyColorElement e = i.next();
            if (r.compareTo("") != 0)
                r += ", ";
            r += e.toString();
        }
        return r;
    }

}
