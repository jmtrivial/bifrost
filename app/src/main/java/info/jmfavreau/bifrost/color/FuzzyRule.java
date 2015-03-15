package info.jmfavreau.bifrost.color;

/**
 * Created by jm on 22/02/15.
 */
public class FuzzyRule {
    private double min0;
    private double min1;
    private double max1;
    private double max0;
    private String name;

    /**
     * return true if v is in [a, b] in a cyclic domain [0, 1]
     * Example: 0.1 is between [0.9; 0.2]
     * @param a left boundary
     * @param b right boundary
     * @param v asked value
     * @return true if v is in the [a, b] interval
     */
    private static Boolean IsBetween(double a, double b, double v) {
        return ((a <= b) && (a <= v) && (v <= b)) ||
                ((a >= b) && ((a >= v) || (v >= b)));
    }

    public FuzzyRule(double min0, double min1, double max1, double max0, String name) {
        this.min0 = min0;
        this.min1 = min1;
        this.max1 = max1;
        this.max0 = max0;
        this.name = name;
    }


    /**
     * Return value of the membership function describe by the current fuzzy rule at abscissa
     * {@code in}
     * @param in
     * @return membership value (between 0 and 1)
     */
    public double getMembershipValue(double in) {
        if (IsBetween(max0, min0, in))
            return 0.;
        else if (IsBetween(min1, max1, in))
            return 1.;
        else if (IsBetween(min0, min1, in))
            return Interpolate(min0, 0., min1, 1., in);
        else {
            assert IsBetween(max1, max0, in);
            return Interpolate(max1, 1., max0, 0., in);
        }
    }

    /**
     * Compute the value at abscissa {@code in} of the affine function defined by f(a0)=v0, f(a1)=v1 in the cyclic
     * domain [0, 1]
     * @param a0 first abscissa
     * @param v0 first ordinate
     * @param a1 second abscissa
     * @param v1 second ordinate
     * @param in request abscissa
     * @return ordinate corresponding to abscissa {@code in}
     */
    private double Interpolate(double a0, double v0, double a1, double v1, double in) {
        assert IsBetween(a0, a1, in);
        if (a0 < a1) {
            double d = (v1 - v0) / (a1 - a0);
            return v0 + d * (in - a0);
        }
        else if (a0 > a1) {
            double a2 = a0 + 1.;
            double d = (v0 - v1) / (a2 - a1);
            if (in > a0)
                return v1 + d * (in - a1);
            else {
                return v1 + d * (in + 1. - a1) - 1.;
            }
        }
        else {
            assert a0 == a1;
            // not correctly defined...
            return (v0 + v1) / 2;
        }
    }

    public String getName() {
        return name;
    }
}
