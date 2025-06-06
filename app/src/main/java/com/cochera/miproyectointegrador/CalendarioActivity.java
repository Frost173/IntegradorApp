package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarioActivity extends AppCompatActivity {

    private static final String TAG = "CalendarioActivity";

    private ImageButton btnHome, btnCalendario, btnPerfil, btnAnterior, btnSiguiente;
    private GridLayout gridCalendario, gridDiasSemana;
    private RecyclerView rvReservas;
    private TextView tvMesAnio, tvReservasTitulo;

    private Calendar calendar;
    private DBHelper dbHelper;
    private ReservaAdapter reservaAdapter;

    private int diaSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        try {
            // 1) Inicializar vistas. Si alguna no existe en el XML, petará aquí.
            btnHome        = findViewById(R.id.btnHome);
            btnCalendario  = findViewById(R.id.btnCalendario);
            btnPerfil      = findViewById(R.id.btnPerfil);
            btnAnterior    = findViewById(R.id.btnAnterior);
            btnSiguiente   = findViewById(R.id.btnSiguiente);
            tvMesAnio      = findViewById(R.id.tvMesAnio);
            gridCalendario = findViewById(R.id.gridCalendario);
            gridDiasSemana = findViewById(R.id.gridDiasSemana);
            rvReservas     = findViewById(R.id.rvReservas);
            tvReservasTitulo = findViewById(R.id.tvReservasTitulo);

            // Verificar que ninguna vista sea null (para no chocar más adelante)
            if (btnHome == null || btnCalendario == null || btnPerfil == null ||
                    btnAnterior == null || btnSiguiente == null || tvMesAnio == null ||
                    gridCalendario == null || gridDiasSemana == null ||
                    rvReservas == null || tvReservasTitulo == null) {
                throw new IllegalStateException("Alguna vista de CalendarioActivity es null. Revisa tus IDs en activity_calendario.xml");
            }

            // 2) Inicializar helper y calendario
            calendar = Calendar.getInstance();
            dbHelper = new DBHelper(this);

            // 3) RecyclerView (solo layoutManager; el adapter se setea cuando haya reservas)
            rvReservas.setLayoutManager(new LinearLayoutManager(this));

            // 4) Preparar mes/día por defecto
            diaSeleccionado = calendar.get(Calendar.DAY_OF_MONTH);
            mostrarCalendario();  // construye la grilla de días
            cargarReservasPorFecha(getFechaFormateada(calendar));  // carga reservas del día actual

            // 5) Botón “<” para mes anterior
            btnAnterior.setOnClickListener(v -> {
                calendar.add(Calendar.MONTH, -1);
                diaSeleccionado = -1;  // Ningún día seleccionado aún en el nuevo mes
                mostrarCalendario();
                limpiarReservas();
            });

            // 6) Botón “>” para mes siguiente
            btnSiguiente.setOnClickListener(v -> {
                calendar.add(Calendar.MONTH, 1);
                diaSeleccionado = -1;
                mostrarCalendario();
                limpiarReservas();
            });

            // 7) Barra inferior: Home, Calendario y Perfil
            btnHome.setOnClickListener(v -> {
                startActivity(new Intent(this, Activity_estacionamientos.class));
                finish();
            });

            btnCalendario.setOnClickListener(v ->
                    Toast.makeText(this, "Ya estás en Calendario", Toast.LENGTH_SHORT).show()
            );

            btnPerfil.setOnClickListener(v ->
                    startActivity(new Intent(this, Perfil.class))
            );

        } catch (Exception e) {
            // Si algo falla en onCreate (por ejemplo una vista es null),
            // mostramos un Toast, hacemos Log y cerramos la actividad para que no quede en estado inconsistente.
            Log.e(TAG, "Error en onCreate de CalendarioActivity", e);
            Toast.makeText(this, "No se pudo abrir el calendario. Revisa la configuración.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Limpia la sección de reservas (quita el adapter y reinicia el título).
     */
    private void limpiarReservas() {
        tvReservasTitulo.setText("Reservas:");
        rvReservas.setAdapter(null);
    }

    /**
     * Construye de cero la grilla de 6 filas x 7 columnas,
     * pintando cada día según su mes, si tiene reserva o si está seleccionado.
     */
    private void mostrarCalendario() {
        gridCalendario.removeAllViews();
        gridDiasSemana.removeAllViews();

        // 1) Mostrar mes y año en español, todo en mayúsculas
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMesAnio.setText(formatoMes.format(calendar.getTime()).toUpperCase());

        gridDiasSemana.setColumnCount(7);
        gridCalendario.setColumnCount(7);

        // 2) Encabezado de días de la semana
        String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (int i = 0; i < 7; i++) {
            TextView tvDiaEnc = new TextView(this);
            tvDiaEnc.setText(diasSemana[i]);
            tvDiaEnc.setGravity(Gravity.CENTER);
            tvDiaEnc.setTextSize(14);
            tvDiaEnc.setPadding(4, 4, 4, 4);
            tvDiaEnc.setTextColor(getResources().getColor(android.R.color.black));

            GridLayout.LayoutParams paramsEnc = new GridLayout.LayoutParams();
            paramsEnc.rowSpec = GridLayout.spec(0);
            paramsEnc.columnSpec = GridLayout.spec(i, 1f);
            paramsEnc.width = 0;
            paramsEnc.height = GridLayout.LayoutParams.WRAP_CONTENT;
            tvDiaEnc.setLayoutParams(paramsEnc);

            gridDiasSemana.addView(tvDiaEnc);
        }

        // 3) Averiguar en qué columna empieza el día 1 del mes actual
        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = tempCal.get(Calendar.DAY_OF_WEEK) - 1; // domingo=0, lunes=1, ...
        int mesActual = calendar.get(Calendar.MONTH);
        // Retrocedemos hasta el domingo anterior (o el mismo día si el 1 cae en domingo)
        tempCal.add(Calendar.DAY_OF_MONTH, -primerDiaSemana);

        // 4) Generar 42 “celdas” (6 filas × 7 columnas)
        for (int i = 0; i < 42; i++) {
            TextView tvDia = new TextView(this);
            int diaDelMes = tempCal.get(Calendar.DAY_OF_MONTH);
            int mesTemp = tempCal.get(Calendar.MONTH);

            tvDia.setText(String.valueOf(diaDelMes));
            tvDia.setGravity(Gravity.CENTER);
            tvDia.setTextSize(16);
            tvDia.setPadding(12, 16, 12, 16);
            tvDia.setClickable(true);

            GridLayout.LayoutParams paramsDia = new GridLayout.LayoutParams();
            paramsDia.width = 0;
            paramsDia.height = GridLayout.LayoutParams.WRAP_CONTENT;
            paramsDia.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            tvDia.setLayoutParams(paramsDia);

            // Fecha “actual” guardada para el listener
            Calendar fechaCelda = (Calendar) tempCal.clone();
            String fechaFormateada = getFechaFormateada(fechaCelda);

            // 5) Estilos según condición:
            if (mesTemp != mesActual) {
                // Días de meses anteriores/próximos en gris
                tvDia.setTextColor(getResources().getColor(android.R.color.darker_gray));
                tvDia.setBackgroundResource(android.R.color.transparent);
            } else if (diaDelMes == diaSeleccionado) {
                // Día actualmente seleccionado: fondo especial
                tvDia.setBackgroundResource(R.drawable.fondo_dia_seleccionado);
                tvDia.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                // Si el día (mesTemp == mesActual) tiene reservas, color verde
                boolean tieneRes = false;
                try {
                    tieneRes = dbHelper.tieneReservasEnFecha(fechaFormateada);
                } catch (Exception e) {
                    Log.e(TAG, "Error comprobando reservas en " + fechaFormateada, e);
                }
                if (tieneRes) {
                    tvDia.setBackgroundColor(getResources().getColor(R.color.verdeReserva));
                    tvDia.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    // Día normal del mes
                    tvDia.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                    tvDia.setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            // 6) Listener para cuando el usuario cliquea un día del mes visible
            final int diaClick = diaDelMes;
            final Calendar fechaClick = (Calendar) tempCal.clone();
            tvDia.setOnClickListener(v -> {
                if (fechaClick.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    diaSeleccionado = diaClick;
                    mostrarCalendario();  // repinta todo el mes con el nuevo díaSeleccionado
                    cargarReservasPorFecha(getFechaFormateada(fechaClick));
                }
            });

            gridCalendario.addView(tvDia);
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * @param cal Calendario con la fecha que queremos formatear.
     * @return cadena "yyyy-MM-dd" de la fecha.
     */
    @SuppressLint("SimpleDateFormat")
    private String getFechaFormateada(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    /**
     * Carga las reservas de la base de datos para la fecha dada y actualiza el RecyclerView.
     * Si ocurre alguna excepción, se atrapa y se muestra un Toast en lugar de crashear.
     */
    private void cargarReservasPorFecha(String fecha) {
        try {
            List<Reserva> reservas = dbHelper.obtenerReservasPorFecha(fecha);
            if (reservas == null || reservas.isEmpty()) {
                tvReservasTitulo.setText("Reservas para " + fecha + ": No hay reservas");
                rvReservas.setAdapter(null);
            } else {
                tvReservasTitulo.setText("Reservas para " + fecha + ":");
                reservaAdapter = new ReservaAdapter(this, reservas);
                rvReservas.setAdapter(reservaAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cargando reservas para " + fecha, e);
            Toast.makeText(this, "No se pudieron cargar las reservas para " + fecha, Toast.LENGTH_SHORT).show();
            rvReservas.setAdapter(null);
            tvReservasTitulo.setText("Reservas para " + fecha + ": Error al cargar");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerramos el helper para no filtrar contexto
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
