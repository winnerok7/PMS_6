package ua.kpi.comsys.iv8107;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import java.lang.Math;


public class DrawingView extends View {
    private String string = "";
    private int color = Color.GREEN;
    private float dimension = 0;
    private int numDrawing = 0;
    private Drawable exampleDrawable;


    private TextPaint TextPaint;

    private final Paint colorGr = new Paint();
    private final Paint colorBlck = new Paint();
    private final Paint colorCn = new Paint();
    private final Paint colorPrpl = new Paint();
    private final Paint colorYlw = new Paint();

    public DrawingView(Context context) {
        super(context);
        DataInit(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DataInit(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DataInit(attrs, defStyle);
    }

    private void DataInit(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawingView, defStyle, 0);

        string = a.getString(
                R.styleable.DrawingView_exampleString);
        color = a.getColor(
                R.styleable.DrawingView_exampleColor,
                color);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        dimension = a.getDimension(
                R.styleable.DrawingView_exampleDimension,
                dimension);

        if (a.hasValue(R.styleable.DrawingView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                    R.styleable.DrawingView_exampleDrawable);
            exampleDrawable.setCallback(this);
        }

        colorBlck.setColor(Color.BLACK);
        colorCn.setColor(Color.CYAN);
        colorPrpl.setColor(0xff6a0dad);
        colorYlw.setColor(Color.YELLOW);
        colorGr.setColor(Color.GRAY);

        colorBlck.setStrokeWidth(5);
        colorPrpl.setStrokeWidth(5);

        a.recycle();

        // Set up a default TextPaint object
        TextPaint = new TextPaint();
        TextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        TextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        TextPaint.setTextSize(dimension);
        TextPaint.setColor(color);
        float mTextWidth = TextPaint.measureText(string);
        Paint.FontMetrics fontMetrics = TextPaint.getFontMetrics();
        float mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int pdnLft = getPaddingLeft();
        int pdnTop = getPaddingTop();
        int pdnRght = getPaddingRight();
        int pdnBttm = getPaddingBottom();

        int contentWidth = getWidth() - pdnLft - pdnRght;
        int contentHeight = getHeight() - pdnTop - pdnBttm;

        if (numDrawing == 0) {
            int n = 50;
            float xPast = 0;
            float yPast = 0;
            colorPrpl.setStyle(Paint.Style.FILL);
            colorPrpl.setStrokeWidth(5);
            for (int i = 0; i < n; i++) {
                float y = contentHeight -
                        (float) (Math.cos(Math.PI * (2 * (double) i / (n - 1) - 1)) + 1 + 1)
                                * contentHeight / 4 + pdnTop;
                float x = (float) i / (n - 1) * contentWidth + pdnLft;
                if (i == 0) {
                    xPast = x;
                    yPast = y;
                } else {
                    canvas.drawLine(xPast, yPast, x, y, colorPrpl);
                    xPast = x;
                    yPast = y;
                }
            }

            canvas.drawLine(0, (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft,
                    contentHeight + pdnTop + pdnBttm, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft - 25, 50, colorBlck);

            canvas.drawLine((float) contentWidth / 2 + pdnLft, 0,
                    (float) contentWidth / 2 + pdnLft + 25, 50, colorBlck);

            canvas.drawLine(contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght - 50,
                    (float) contentHeight / 2 + pdnTop - 25, colorBlck);

            canvas.drawLine(contentWidth + pdnLft + pdnRght,
                    (float) contentHeight / 2 + pdnTop,
                    contentWidth + pdnLft + pdnRght - 50,
                    (float) contentHeight / 2 + pdnTop + 25, colorBlck);
        } else if (numDrawing == 1) {
            int size = Math.min(contentWidth, contentHeight);
            int xStart = pdnLft + (contentWidth - size) / 2;
            int yStart = pdnTop + (contentHeight - size) / 2;
            RectF oval = new RectF(xStart, yStart, xStart + size, yStart + size);
            colorCn.setStyle(Paint.Style.STROKE);
            colorPrpl.setStyle(Paint.Style.STROKE);
            colorYlw.setStyle(Paint.Style.STROKE);
            colorGr.setStyle(Paint.Style.STROKE);
            colorCn.setStrokeWidth(size / 10);
            colorPrpl.setStrokeWidth(size / 10);
            colorYlw.setStrokeWidth(size / 10);
            colorGr.setStrokeWidth(size / 10);
            canvas.drawArc(oval, 0F, 162F, false, colorCn);
            canvas.drawArc(oval, 162F, 18F, false, colorPrpl);
            canvas.drawArc(oval, 180F, 90F, false, colorYlw);
            canvas.drawArc(oval, 270F, 90F, false, colorGr);
        }
    }

    public void setNumberOfDrawing(int numberOfDrawing) {
        numDrawing = numberOfDrawing;
    }

    public int getNumberOfDrawing() {
        return numDrawing;
    }

}

