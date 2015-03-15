package info.jmfavreau.bifrost.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import info.jmfavreau.bifrost.color.HSLColor;

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

    public void redraw() {
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
/*                int x = Math.max( 0, Math.min( bitmap.getWidth() - 1, (int)event.getX() ) );
                float value = x / (float)bitmap.getWidth();
                if ( colorHsv[2] != value ) {
                    colorHsv[2] = value;
                    if ( listener != null ) {
                        listener.colorSelected( Color.HSVToColor( colorHsv ) );
                    }
                    redrawBitmap();
                    invalidate();
                }*/
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
        int w = rect.width();
        int h = rect.height();
        int pixels[] = new int[w * h];
        HSLColor c_tmp = color;

        for(int i = 0; i != w; ++i) {
            int v = updateColorFromCursorValue(c_tmp, (double) i / (w - 1));
            for(int j = 0; j != h; ++j) {
                pixels[i * h + j] = v;
            }
        }

        drawCursor();
    }

    protected abstract int updateColorFromCursorValue(HSLColor c, double v);
    protected abstract double getValueFromHSLColor();
    protected abstract void setColorFromValue(double c);

    void drawCursor() {
        // TODO

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
