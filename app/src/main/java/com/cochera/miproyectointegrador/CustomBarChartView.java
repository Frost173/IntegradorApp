package com.cochera.miproyectointegrador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomBarChartView extends View {

    private final Paint barPaint = new Paint();
    private final Paint textPaint = new Paint();
    private Map<String, Integer> data = new LinkedHashMap<>();

    public CustomBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint.setColor(Color.parseColor("#3F51B5"));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(26f);
        textPaint.setAntiAlias(true);
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
        int barWidth = width / (data.size() * 2);
        int maxVal = 1;

        for (int val : data.values()) {
            if (val > maxVal) maxVal = val;
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            float left = i * 2 * barWidth + barWidth / 2f;
            float barHeight = (entry.getValue() * (height * 0.7f)) / maxVal;
            float top = height - barHeight - 40;
            float right = left + barWidth;
            float bottom = height - 40;

            canvas.drawRect(left, top, right, bottom, barPaint);
            canvas.drawText(entry.getKey(), left, height - 10, textPaint);
            canvas.drawText(String.valueOf(entry.getValue()), left + barWidth / 4f, top - 10, textPaint);
            i++;
        }
    }
}
