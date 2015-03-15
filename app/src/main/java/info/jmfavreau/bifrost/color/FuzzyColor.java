package info.jmfavreau.bifrost.color;

import android.content.res.Resources;

import java.util.List;

/**
 * Created by jm on 22/02/15.
 */
public class FuzzyColor {
    private List<FuzzyColorElement> hue;
    private List<FuzzyColorElement> saturation;
    private List<FuzzyColorElement> lightness;
    private HSLColor color;

    /**
     * Initialization of a fuzzy color using an RGB description (each component between 0 and 1)
     * @param r
     * @param g
     * @param b
     */
    public FuzzyColor(double r, double g, double b) throws Resources.NotFoundException {
        color = HSLColor.fromRGB(r, g, b);
        setFuzzyLabels();
    }

    private void setFuzzyLabels() throws Resources.NotFoundException {
        FuzzyColorRules rules = FuzzyColorRules.getInstance();
        hue = rules.getFuzzyDescriptionHue(color.getHue());
        saturation = rules.getFuzzyDescriptionSaturation(color.getSaturation());
        lightness = rules.getFuzzyDescriptionLightness(color.getLightness());
    }


}
