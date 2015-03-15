package info.jmfavreau.bifrost.ui;

import android.content.Context;
import android.util.AttributeSet;

import info.jmfavreau.bifrost.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 15/03/15.
 */
public class LightnessBarSelector extends ColorBarSelector {
    public LightnessBarSelector(Context context) {
        super(context);
    }

    public LightnessBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightnessBarSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected double getValueFromHSLColor() {
        return color.getLightness();
    }

    protected void setColorFromValue(double c) {
        color.setLightness(c);
    }

    protected int updateColorFromCursorValue(HSLColor c, double v) {
        c.setLightness(v);
        return c.getIntColor();
    }


}
