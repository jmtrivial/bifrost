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
