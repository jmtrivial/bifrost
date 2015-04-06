package info.jmfavreau.bifrost.color;

import android.util.Log;

import java.util.List;

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
    public String toString(Boolean values) {
        if (values)
            return String.format( "%s (%.4f)", name, value);
        else
            return String.format( "%s", name);
    }

    public static FuzzyColorElement getMainComponent(List<FuzzyColorElement> list) {
        double v = -1;
        FuzzyColorElement result = null;
        for(FuzzyColorElement e: list)
            if (e.getValue() > v) {
                v = e.getValue();
                result = e;
            }
        return result;
    }
}
