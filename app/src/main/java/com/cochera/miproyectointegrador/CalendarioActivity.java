package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private TextView ultimoSeleccionado;

    private Calendar calendar;
    private DBHelper dbHelper;
    private ReservaAdapter reservaAdapter;
    private int diaSeleccionado = -1;
    private int usuarioActualId = -1;

    private FirebaseUser usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

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

        usuarioFirebase = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioFirebase != null) {
            usuarioActualId = dbHelper.obtenerUsuarioIdPorUid(usuarioFirebase.getUid());
            Log.d(TAG, "Usuario Firebase UID: " + usuarioFirebase.getUid() + " | UsuarioID SQLite: " + usuarioActualId);
        } else {
            Toast.makeText(this, "No hay sesión iniciada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rvReservas.setLayoutManager(new LinearLayoutManager(this));
        diaSeleccionado = calendar.get(Calendar.DAY_OF_MONTH);

        mostrarCalendario();
        cargarReservasPorFecha(getFechaFormateada(calendar));
    }

    private void configurarListeners() {
        btnAnterior.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            diaSeleccionado = -1;
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

        btnCalendario.setOnClickListener(v -> {
            Toast.makeText(this, "Ya estás en el calendario", Toast.LENGTH_SHORT).show();
        });

        btnPerfil.setOnClickListener(v -> {
            startActivity(new Intent(this, Perfil.class));
        });
    }

    private void limpiarReservas() {
        tvReservasTitulo.setText("Reservas:");
        rvReservas.setAdapter(null);
    }


    private void mostrarCalendario() {
        gridCalendario.removeAllViews();
        gridDiasSemana.removeAllViews();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdfMes = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMesAnio.setText(sdfMes.format(calendar.getTime()).toUpperCase());

        // Días de la semana
        String[] dias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String dia : dias) {
            agregarTextoAGrid(dia, gridDiasSemana, 14, R.color.colorTextoTitulo);
        }

        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        tempCal.add(Calendar.DAY_OF_MONTH, -primerDiaSemana);

        int mesActual = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 42; i++) {
            final Calendar fechaDia = (Calendar) tempCal.clone();
            final String fechaFormateada = getFechaFormateada(fechaDia);
            int dia = tempCal.get(Calendar.DAY_OF_MONTH);

            final TextView tv = new TextView(this);
            tv.setText(String.valueOf(dia));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            tv.setPadding(16, 16, 16, 16);
            tv.setTag(fechaFormateada);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // distribuir uniformemente
            params.setMargins(8, 8, 8, 8); // espacio entre celdas
            tv.setLayoutParams(params);

            if (tempCal.get(Calendar.MONTH) != mesActual) {
                tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                boolean tieneReserva = dbHelper.tieneReservasEnFechaYUsuario(fechaFormateada, usuarioActualId);
                if (tieneReserva) {
                    tv.setBackgroundResource(R.drawable.bg_reserva); // fondo verde claro
                } else {
                    tv.setBackgroundResource(R.drawable.bg_normal); // fondo blanco con borde
                }

                tv.setOnClickListener(v -> {
                    if (ultimoSeleccionado != null) {
                        // Restaurar el anterior
                        String fechaAnt = (String) ultimoSeleccionado.getTag();
                        boolean reservaAnt = dbHelper.tieneReservasEnFechaYUsuario(fechaAnt, usuarioActualId);
                        if (reservaAnt) {
                            ultimoSeleccionado.setBackgroundResource(R.drawable.bg_reserva);
                        } else {
                            ultimoSeleccionado.setBackgroundResource(R.drawable.bg_normal);
                        }
                    }

                    // Marcar nuevo
                    tv.setBackgroundResource(R.drawable.bg_seleccionado);
                    ultimoSeleccionado = tv;

                    cargarReservasPorFecha(fechaFormateada);
                });
            }

            gridCalendario.addView(tv);
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    private void agregarTextoAGrid(String texto, GridLayout grid, int sizeSp, int color) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(sizeSp);
        tv.setPadding(8, 8, 8, 8);
        tv.setTextColor(getResources().getColor(color));
        grid.addView(tv);
    }



    private String getFechaFormateada(Calendar cal) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private void cargarReservasPorFecha(String fecha) {
        try {
            List<Reserva> reservas = dbHelper.obtenerReservasPorFechaYUsuario(fecha, usuarioActualId);

            if (reservas == null || reservas.isEmpty()) {
                tvReservasTitulo.setText("Reservas para " + fecha + ": No hay reservas");
                rvReservas.setAdapter(null);
            } else {
                tvReservasTitulo.setText("Reservas para " + fecha + ":");
                reservaAdapter = new ReservaAdapter(this, reservas);
                rvReservas.setAdapter(reservaAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cargando reservas", e);
            Toast.makeText(this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
