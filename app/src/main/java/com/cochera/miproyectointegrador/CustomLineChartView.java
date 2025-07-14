package com.cochera.miproyectointegrador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomLineChartView extends View {

    private final Paint linePaint = new Paint();
    private final Paint pointPaint = new Paint();
    private final Paint textPaint = new Paint();
    private Map<String, Integer> data = new LinkedHashMap<>();

    public CustomLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setColor(Color.parseColor("#009688"));
        linePaint.setStrokeWidth(4f);
        linePaint.setAntiAlias(true);

        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
    }

    public void setData(Map<String, Integer> newData) {
        this.data = newData;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int maxVal = 1;
        for (int val : data.values()) if (val > maxVal) maxVal = val;

        List<String> labels = new ArrayList<>(data.keySet());
        int space = width / (labels.size() + 1);

        for (int i = 0; i < labels.size() - 1; i++) {
            float x1 = (i + 1) * space;
            float y1 = height - (data.get(labels.get(i)) * (height - 60) / maxVal);
            float x2 = (i + 2) * space;
            float y2 = height - (data.get(labels.get(i + 1)) * (height - 60) / maxVal);

            canvas.drawLine(x1, y1, x2, y2, linePaint);
            canvas.drawCircle(x1, y1, 8f, pointPaint);
            canvas.drawText(String.valueOf(data.get(labels.get(i))), x1 - 10, y1 - 10, textPaint);
        }

        // último punto
        float xLast = labels.size() * space;
        float yLast = height - (data.get(labels.get(labels.size() - 1)) * (height - 60) / maxVal);
        canvas.drawCircle(xLast, yLast, 8f, pointPaint);
        canvas.drawText(String.valueOf(data.get(labels.get(labels.size() - 1))), xLast - 10, yLast - 10, textPaint);
    }
}
