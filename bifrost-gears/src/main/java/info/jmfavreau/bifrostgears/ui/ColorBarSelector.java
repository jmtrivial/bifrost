/*
 * Bifröst - A camera-to-speech system for Android
 * Copyright (C) 2015 Jean-Marie Favreau <jeanmarie.favreau@free.fr>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package info.jmfavreau.bifrostgears.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import info.jmfavreau.bifrostcore.color.HSLColor;

/**
 * Created by Jean-Marie Favreau on 15/03/15.
 */
abstract public class ColorBarSelector extends View {
    private static final int POINTER_LINE_WIDTH_DP = 2;
    private static final int POINTER_LENGTH_DP = 10;



    protected HSLColor color;
    private View.OnClickListener listener;

    public ColorBarSelector(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ColorBarSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        color = HSLColor.red();
        init();
    }

    public ColorBarSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        color = HSLColor.red();
        init();
    }

    public void setColor(HSLColor c) {
        this.color = c;
    }

    public HSLColor getColor() {
        return this.color;
    }

    public void redraw() {
        redrawBitmap();
        invalidate();
    }

    public void setColorChangedListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x = Math.max( 0, Math.min( bitmap.getWidth() - 1, (int)event.getX() ) );
                float value = x / ((float)bitmap.getWidth() - 1);
                if (getValueFromHSLColor() != value) {
                    setColorFromValue(value);
                    if (listener != null) {
                        listener.onClick(this);
                    }
                    redrawBitmap();
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private Rect rect;
    private Bitmap bitmap;
    private int innerPadding;
    private Context context;

    // a tool used by the drawCursor method
    private Paint pointerPaint = new Paint();

    private int pointerLength;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rect = new Rect(innerPadding, innerPadding, w - innerPadding, h - innerPadding);

        bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888 );
        redrawBitmap();
    }

    void redrawBitmap() {
        if (bitmap != null) {
            int w = rect.width();
            int h = rect.height();
            int pixels[] = new int[w * h];
            HSLColor c_tmp = new HSLColor(color);

            for(int i = 0; i != w; ++i) {
                int v = updateColorFromCursorValue(c_tmp, (double) i / (w - 1));
                for(int j = 0; j != h; ++j) {
                    pixels[j * w + i] = v;
                }
            }

            drawCursor(pixels, w, h);
        }
    }

    protected abstract int updateColorFromCursorValue(HSLColor c, double v);
    protected abstract double getValueFromHSLColor();
    protected abstract void setColorFromValue(double c);

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    void drawCursor(int pixels[], int w, int h) {
        double v = getValueFromHSLColor();
        int c = (int)(v * (double)w);

        for(int j = 0; j != pointerLength; ++j) {
            for(int i = c - pointerLength + j; i != c + pointerLength + 1 - j; ++i) {
                if (i >= 0 && i < w) {
                     pixels[j * w + i] = blendColors(pixels[j * w + i], Color.WHITE, (float)0.5);
                     pixels[(h - j - 1) * w + i] = blendColors(pixels[j * w + i], Color.WHITE, (float)0.5);
                }
            }
        }



        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);

        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if ( bitmap != null ) {
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }

    private void init() {
        float density = context.getResources().getDisplayMetrics().density;
        pointerLength = (int) (density * POINTER_LENGTH_DP );
        pointerPaint.setStrokeWidth(  (int) (density * POINTER_LINE_WIDTH_DP ) );
        innerPadding = pointerLength / 2;
        color = HSLColor.red();
    }

}
