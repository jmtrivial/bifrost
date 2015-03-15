package info.jmfavreau.bifrost.ui;

import android.content.Context;
import android.util.AttributeSet;

import info.jmfavreau.bifrost.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 15/03/15.
 */
public class SaturationBarSelector extends ColorBarSelector {
    public SaturationBarSelector(Context context) {
        super(context);
    }

    public SaturationBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaturationBarSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected double getValueFromHSLColor() {
        return color.getSaturation();
    }

    protected void setColorFromValue(double c) {
        color.setSaturation(c);
    }

    protected int updateColorFromCursorValue(HSLColor c, double v) {
        c.setSaturation(v);
        return c.getIntColor();
    }
}
