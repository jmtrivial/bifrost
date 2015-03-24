package info.jmfavreau.bifrost.ui;

import android.content.Context;
import android.util.AttributeSet;

import info.jmfavreau.bifrost.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 15/03/15.
 */
public class HueBarSelector extends ColorBarSelector {
    public HueBarSelector(Context context) {
        super(context);
    }

    public HueBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HueBarSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected double getValueFromHSLColor() {
        return color.getHue();
    }

    protected void setColorFromValue(double c) {
        color.setHue(c);
    }

    @Override
    public void redraw() {
        invalidate();
    }

    protected int updateColorFromCursorValue(HSLColor c, double v) {
        c.setLightness(.5);
        c.setSaturation(1.);
        c.setHue(v);
        return c.getIntColor();
    }
}
