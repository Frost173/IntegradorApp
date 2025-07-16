package com.cochera.miproyectointegrador;

import android.content.Context;
import android.graphics.Bitmap;
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
    private final Paint valuePaint = new Paint();
    private Map<String, Integer> data = new LinkedHashMap<>();

    public CustomBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint.setColor(Color.parseColor("#4CAF50")); // verde
        barPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER); // centrado horizontal
        textPaint.setAntiAlias(true);

        valuePaint.setColor(Color.BLACK);
        valuePaint.setTextSize(26f);
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setAntiAlias(true);
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

        int paddingBottom = 60;
        int paddingTop = 40;
        int paddingSides = 40;

        int availableWidth = width - 2 * paddingSides;
        int maxVal = 1;
        for (int val : data.values()) {
            if (val > maxVal) maxVal = val;
        }

        int barCount = data.size();
        int barSpacing = 20;
        int totalBarSpace = availableWidth - (barSpacing * (barCount - 1));
        int barWidth = totalBarSpace / barCount;

        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            float left = paddingSides + i * (barWidth + barSpacing);
            float barHeight = ((float) entry.getValue() / maxVal) * (height - paddingTop - paddingBottom);
            float top = height - paddingBottom - barHeight;
            float right = left + barWidth;
            float bottom = height - paddingBottom;

            // Dibuja la barra
            canvas.drawRect(left, top, right, bottom, barPaint);

            // Dibuja el valor encima
            canvas.drawText(String.valueOf(entry.getValue()), left + barWidth / 2f, top - 10, valuePaint);

            // Etiqueta del eje X (recta)
            String label = entry.getKey();
            canvas.drawText(label, left + barWidth / 2f, height - 10, textPaint);

            i++;
        }
    }

    public Bitmap getChartBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}
