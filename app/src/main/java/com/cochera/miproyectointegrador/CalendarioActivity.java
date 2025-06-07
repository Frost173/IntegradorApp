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

    // Vistas
    private ImageButton btnHome, btnCalendario, btnPerfil, btnAnterior, btnSiguiente;
    private GridLayout gridCalendario, gridDiasSemana;
    private RecyclerView rvReservas;
    private TextView tvMesAnio, tvReservasTitulo;

    // Variables
    private Calendar calendar;
    private DBHelper dbHelper;
    private ReservaAdapter reservaAdapter;
    private int diaSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        // Inicializar vistas y componentes
        inicializarVistas();
        inicializarComponentes();
        configurarListeners();
    }

    private void inicializarVistas() {
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        tvMesAnio = findViewById(R.id.tvMesAnio);
        gridCalendario = findViewById(R.id.gridCalendario);
        gridDiasSemana = findViewById(R.id.gridDiasSemana);
        rvReservas = findViewById(R.id.rvReservas);
        tvReservasTitulo = findViewById(R.id.tvReservasTitulo);
    }

    private void inicializarComponentes() {
        calendar = Calendar.getInstance();
        dbHelper = new DBHelper(this);

        rvReservas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializamos el día seleccionado en el día actual
        diaSeleccionado = calendar.get(Calendar.DAY_OF_MONTH);

        // Mostrar calendario y cargar reservas del mes y día actuales
        mostrarCalendario();
        cargarReservasPorFecha(getFechaFormateada(calendar));
    }

    private void configurarListeners() {
        btnAnterior.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            diaSeleccionado = -1; // No seleccionamos día al cambiar mes
            mostrarCalendario();
            limpiarReservas();
        });

        btnSiguiente.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            diaSeleccionado = -1;
            mostrarCalendario();
            limpiarReservas();
        });

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
    }

    private void limpiarReservas() {
        tvReservasTitulo.setText("Reservas:");
        rvReservas.setAdapter(null);
    }

    private void mostrarCalendario() {
        gridCalendario.removeAllViews();
        gridDiasSemana.removeAllViews();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMesAnio.setText(formatoMes.format(calendar.getTime()).toUpperCase());

        gridDiasSemana.setColumnCount(7);
        gridCalendario.setColumnCount(7);

        // Mostrar días de la semana
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

        // Calcular desde qué día iniciar el calendario (el domingo anterior o mismo día 1 si es domingo)
        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = tempCal.get(Calendar.DAY_OF_WEEK) - 1; // Domingo=1, restamos 1 para índice base 0
        tempCal.add(Calendar.DAY_OF_MONTH, -primerDiaSemana);

        int mesActual = calendar.get(Calendar.MONTH);

        // Crear las 6 semanas (42 días)
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

            Calendar fechaCelda = (Calendar) tempCal.clone();
            String fechaFormateada = getFechaFormateada(fechaCelda);

            if (mesTemp != mesActual) {
                // Días de otros meses en gris y no clickeables
                tvDia.setTextColor(getResources().getColor(android.R.color.darker_gray));
                tvDia.setBackgroundResource(android.R.color.transparent);
                tvDia.setClickable(false);
            } else if (diaDelMes == diaSeleccionado) {
                // Día seleccionado con fondo especial
                tvDia.setBackgroundResource(R.drawable.fondo_dia_seleccionado);
                tvDia.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                // Verificar si tiene reservas
                boolean tieneRes = false;
                try {
                    tieneRes = dbHelper.tieneReservasEnFecha(fechaFormateada);
                } catch (Exception e) {
                    Log.e(TAG, "Error comprobando reservas en " + fechaFormateada, e);
                }

                if (tieneRes) {
                    // Días con reservas en verde
                    tvDia.setBackgroundColor(getResources().getColor(R.color.verdeReserva));
                    tvDia.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    tvDia.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                    tvDia.setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            if (mesTemp == mesActual) {
                final int diaClick = diaDelMes;
                final Calendar fechaClick = (Calendar) tempCal.clone();
                tvDia.setOnClickListener(v -> {
                    diaSeleccionado = diaClick;
                    mostrarCalendario();
                    cargarReservasPorFecha(getFechaFormateada(fechaClick));
                });
            }

            gridCalendario.addView(tvDia);
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getFechaFormateada(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

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
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

