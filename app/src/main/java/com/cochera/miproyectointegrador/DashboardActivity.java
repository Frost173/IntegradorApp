package com.cochera.miproyectointegrador;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.CustomBarChartView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtReservasPendientes, txtVehiculoTop,
            txtUsuarioTop1, txtUsuarioTop2, txtUsuarioTop3;
    private Button btnExportarPDF;
    private CustomBarChartView barChartView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtReservasPendientes = findViewById(R.id.txtReservasPendientes);
        txtVehiculoTop = findViewById(R.id.txtVehiculoTop);
        txtUsuarioTop1 = findViewById(R.id.txtUsuarioTop1);
        txtUsuarioTop2 = findViewById(R.id.txtUsuarioTop2);
        txtUsuarioTop3 = findViewById(R.id.txtUsuarioTop3);
        btnExportarPDF = findViewById(R.id.btnExportarPDF);
        barChartView = findViewById(R.id.barChartView);

        dbHelper = new DBHelper(this);

        cargarReservasPendientes();
        cargarVehiculoTop();
        cargarTopUsuarios();
        cargarGraficoBarras();

        btnExportarPDF.setOnClickListener(v -> exportarReportePDF());
    }

    private void cargarReservasPendientes() {
        int pendientes = dbHelper.contarReservasPendientes();
        txtReservasPendientes.setText("Reservas pendientes: " + pendientes);
    }

    private void cargarVehiculoTop() {
        String tipoVehiculo = dbHelper.obtenerTipoVehiculoMasUsado();
        txtVehiculoTop.setText("🚗 Tipo de vehículo más usado: " + tipoVehiculo);
    }

    private void cargarTopUsuarios() {
        List<String> topUsuarios = dbHelper.obtenerTop3Usuarios();
        txtUsuarioTop1.setText(topUsuarios.size() > 0 ? "1. " + topUsuarios.get(0) : "1. -");
        txtUsuarioTop2.setText(topUsuarios.size() > 1 ? "2. " + topUsuarios.get(1) : "2. -");
        txtUsuarioTop3.setText(topUsuarios.size() > 2 ? "3. " + topUsuarios.get(2) : "3. -");
    }

    private void cargarGraficoBarras() {
        Cursor cursor = dbHelper.obtenerReservasPorEstacionamiento();
        Map<String, Integer> datos = new LinkedHashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("estacionamiento"));
                int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                datos.put(nombre, total);
            } while (cursor.moveToNext());
            cursor.close();
        }

        barChartView.setData(datos);
    }

    private void exportarReportePDF() {
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        int y = 40;

        paint.setTextSize(18f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("📄 Reporte de Reservas", pageInfo.getPageWidth() / 2f, y, paint);
        y += 25;

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(40, y, pageInfo.getPageWidth() - 40, y, paint);
        y += 20;

        paint.setTextSize(16f);
        canvas.drawText(txtReservasPendientes.getText().toString(), 40, y, paint); y += 25;
        canvas.drawText(txtVehiculoTop.getText().toString(), 40, y, paint); y += 25;
        canvas.drawText(txtUsuarioTop1.getText().toString(), 40, y, paint); y += 20;
        canvas.drawText(txtUsuarioTop2.getText().toString(), 40, y, paint); y += 20;
        canvas.drawText(txtUsuarioTop3.getText().toString(), 40, y, paint); y += 20;

        Bitmap chartBitmap = barChartView.getChartBitmap();
        if (chartBitmap != null) {
            canvas.drawBitmap(chartBitmap, 40, y + 20, null);
        }

        document.finishPage(page);

        // Ruta a carpeta Descargas pública
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        File file = new File(downloadDir, "ReporteReservas.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();

            Toast.makeText(this, "PDF guardado en Descargas:\n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_SHORT).show();
            Log.e("PDF", "IOException: " + e.getMessage(), e);
        }
    }

}
