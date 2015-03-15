package info.jmfavreau.bifrost.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jmfavreau.bifrost.R;
import info.jmfavreau.bifrost.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 23/02/15.
 */
// https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/HSVColorPickerDialog.java
public class HSLColorPicker extends LinearLayout implements View.OnClickListener {


    private LightnessBarSelector ls;
    private SaturationBarSelector ss;
    private HueBarSelector hs;
    private ColorViewer cv;
    private TextView tv;

    private HSLColor c;

    public void onClick(View v) {
        ls.redraw();
        ss.redraw();
        hs.redraw();
        cv.setBackgroundColor(c.getIntColor());
        String text = "h: " + String.valueOf(c.getHue()) + " s: " + String.valueOf(c.getSaturation()) + " l: " + String.valueOf(c.getLightness());
        tv.setText(text.toString());
    }

    public HSLColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.hsl_color_picker, this);

        c = HSLColor.red();
    }

    protected void onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        hs = (HueBarSelector) findViewById(R.id.myHueSelector);
        ss = (SaturationBarSelector) findViewById(R.id.mySaturationSelector);
        ls = (LightnessBarSelector) findViewById(R.id.myLightnessSelector);
        cv = (ColorViewer) findViewById(R.id.myColorViewer);
        tv = (TextView) findViewById(R.id.myTextColor);

        ss.setColorChangedListener(this);
        hs.setColorChangedListener(this);
        ls.setColorChangedListener(this);
        ss.setColor(c);
        hs.setColor(c);
        ls.setColor(c);
    }



    public HSLColor getColor() {
        return c;
    }

}
