package info.jmfavreau.bifrost.color;

/**
 * Created by jm on 22/02/15.
 */
public class FuzzyColorElement {

    private String name;
    private double value;

    public double getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    public FuzzyColorElement(String name, double value) {
        if (value < 0.)
            this.value = 0.;
        else if (value > 1.)
            this.value = 1.;
        else
            this.value = value;
        this.name = name;
    }
    public String toString() {
        String text = String.format( "%s (%.4f)", name, value);
        return text;
    }
}
