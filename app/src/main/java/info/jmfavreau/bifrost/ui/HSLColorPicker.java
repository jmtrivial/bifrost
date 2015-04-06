package info.jmfavreau.bifrost.ui;

import android.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jmfavreau.bifrost.R;
import info.jmfavreau.bifrost.color.FuzzyColor;
import info.jmfavreau.bifrost.color.HSLColor;
import info.jmfavreau.bifrost.color.SemanticColor;
import info.jmfavreau.bifrost.color.SemanticColorRules;

/**
 * Created by Jean-Marie Favreau on 23/02/15.
 */
// https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/HSVColorPickerDialog.java
public class HSLColorPicker extends Fragment implements View.OnClickListener {


    private LightnessBarSelector ls;
    private SaturationBarSelector ss;
    private HueBarSelector hs;
    private ColorViewer cv;
    private EditText tv;

    private HSLColor c;

    public HSLColorPicker() {
        super();

    }


    public void onClick(View v) {
        if (v.getId() == hs.getId()) {
            c = hs.getColor();
            ls.setColor(c);
            ss.setColor(c);
        }
        else if (v.getId() == ss.getId()) {
            c = ss.getColor();
            ls.setColor(c);
            hs.setColor(c);
        }
        else if (v.getId() == ls.getId()) {
            c = ls.getColor();
            ss.setColor(c);
            hs.setColor(c);
        }
        update();
    }

    public void update() {

        ls.redraw();
        ss.redraw();
        hs.redraw();
        cv.setBackgroundColor(c.getIntColor());

        FuzzyColor f = new FuzzyColor(c);
        SemanticColor s = SemanticColorRules.toSemantic(f);
        f.defuzzificationUnary();
        String text = String.format( "h: %.4f, s: %4f, l: %4f\nr: %.4f, g: %.4f, b: %.4f\nfuzzy: %s\nsemantic: %s",
                c.getHue(), c.getSaturation(), c.getLightness(),
                c.getRed(), c.getGreen(), c.getBlue(), f.toString(true), s.toString());
        tv.setText(text.toString());
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.hsl_color_picker, container, false);

        c = HSLColor.red();
        hs = (HueBarSelector) view.findViewById(R.id.myHueSelector);
        ss = (SaturationBarSelector) view.findViewById(R.id.mySaturationSelector);
        ls = (LightnessBarSelector) view.findViewById(R.id.myLightnessSelector);
        cv = (ColorViewer) view.findViewById(R.id.myColorViewer);
        tv = (EditText) view.findViewById(R.id.myTextColor);

        ss.setColorChangedListener(this);
        hs.setColorChangedListener(this);
        ls.setColorChangedListener(this);
        ss.setColor(c);
        hs.setColor(c);
        ls.setColor(c);

        update();

        return view;
    }



    public HSLColor getColor() {
        return c;
    }

}
