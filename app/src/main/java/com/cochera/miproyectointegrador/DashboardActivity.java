package com.cochera.miproyectointegrador;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.CustomBarChartView;
import com.cochera.miproyectointegrador.CustomLineChartView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtReservasHoy, txtReservasMes, txtHorarioTop,
            txtUsuarioTop1, txtUsuarioTop2, txtUsuarioTop3, txtVehiculoTop;
    private CustomBarChartView barChartView;
    private CustomLineChartView lineChartView;
    private Button btnExportarPDF;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Vinculación con vistas
        txtReservasHoy = findViewById(R.id.txtReservasHoy);
        txtReservasMes = findViewById(R.id.txtReservasMes);
        txtHorarioTop = findViewById(R.id.txtHorarioTop);
        txtUsuarioTop1 = findViewById(R.id.txtUsuarioTop1);
        txtUsuarioTop2 = findViewById(R.id.txtUsuarioTop2);
        txtUsuarioTop3 = findViewById(R.id.txtUsuarioTop3);
        txtVehiculoTop = findViewById(R.id.txtVehiculoTop);
        barChartView = findViewById(R.id.barChartView);
        lineChartView = findViewById(R.id.lineChartView);
        btnExportarPDF = findViewById(R.id.btnExportarPDF);

        dbHelper = new DBHelper(this);

        cargarResumen();
        cargarGraficoBarras();
        cargarGraficoLineas();
        btnExportarPDF.setOnClickListener(v -> exportarReportePDF());
    }

    private void cargarResumen() {
        int hoy = dbHelper.obtenerReservasHoy();
        int mes = dbHelper.obtenerReservasDelMes();
        txtReservasHoy.setText("Hoy: " + hoy + " reservas");
        txtReservasMes.setText("Mes: " + mes + " reservas");

        // Horario más activo
        Cursor cursor = dbHelper.obtenerHorarioMasFrecuente();
        if (cursor != null && cursor.moveToFirst()) {
            String hora = cursor.getString(cursor.getColumnIndexOrThrow("horaentrada"));
            int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
            txtHorarioTop.setText("Horario más activo: " + hora + " (" + cantidad + " reservas)");
            cursor.close();
        } else {
            txtHorarioTop.setText("Horario más activo: -");
        }

        // Top 3 usuarios
        List<String> topUsuarios = dbHelper.obtenerTop3Usuarios();
        txtUsuarioTop1.setText(topUsuarios.size() > 0 ? "1. " + topUsuarios.get(0) : "1. -");
        txtUsuarioTop2.setText(topUsuarios.size() > 1 ? "2. " + topUsuarios.get(1) : "2. -");
        txtUsuarioTop3.setText(topUsuarios.size() > 2 ? "3. " + topUsuarios.get(2) : "3. -");

        // Tipo de vehículo más usado
        String tipoVehiculo = dbHelper.obtenerTipoVehiculoMasUsado();
        txtVehiculoTop.setText("Tipo de vehículo más usado: " + tipoVehiculo);
    }

    private void cargarGraficoBarras() {
        Cursor cursor = dbHelper.obtenerReservasPorEstacionamiento();
        Map<String, Integer> datos = new LinkedHashMap<>();

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("estacionamiento"));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            datos.put(nombre, total);
        }
        cursor.close();

        barChartView.setData(datos);
    }

    private void cargarGraficoLineas() {
        Cursor cursor = dbHelper.obtenerReservasPorDiaUltimoMes();
        Map<String, Integer> datos = new LinkedHashMap<>();

        while (cursor.moveToNext()) {
            String dia = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            datos.put(dia, total);
        }
        cursor.close();

        lineChartView.setData(datos);
    }

    private void exportarReportePDF() {
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int y = 30;
        paint.setTextSize(14f);

        canvas.drawText("📄 Reporte de Reservas", 10, y, paint); y += 25;
        canvas.drawText(txtReservasHoy.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtReservasMes.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtHorarioTop.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtVehiculoTop.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtUsuarioTop1.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtUsuarioTop2.getText().toString(), 10, y, paint); y += 20;
        canvas.drawText(txtUsuarioTop3.getText().toString(), 10, y, paint); y += 20;

        document.finishPage(page);

        try {
            File file = new File(getExternalFilesDir(null), "ReporteReservas.pdf");
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF generado: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}
