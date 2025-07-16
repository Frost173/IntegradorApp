package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistorialReserva extends AppCompatActivity {

    private RecyclerView recyclerReservas;
    private ReservaAdapter reservaAdapter;
    private List<Reserva> reservaList;
    private DBHelper dbHelper;
    private ImageButton btnHome, btnPerfil;

    private EditText etBuscarPlaca;
    private Spinner spinnerFiltro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reserva);

        recyclerReservas = findViewById(R.id.recyclerReservas);
        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));

        btnHome = findViewById(R.id.btnHome);
        btnPerfil = findViewById(R.id.btnPerfil);
        etBuscarPlaca = findViewById(R.id.etBuscarPlaca);
        spinnerFiltro = findViewById(R.id.spinnerFiltro);

        // Filtros: cambiamos "Estado" por "Placa"
        String[] opcionesFiltro = {"Todos", "Nombre", "Estacionamiento", "Placa"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesFiltro);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapterSpinner);

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityAdminint.class));
            finish();
        });

        btnPerfil.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityPerfilAdmin.class));
            finish();
        });

        dbHelper = new DBHelper(this);
        reservaList = new ArrayList<>();
        reservaAdapter = new ReservaAdapter(this, reservaList);
        recyclerReservas.setAdapter(reservaAdapter);

        cargarTodasLasReservas();

        etBuscarPlaca.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spinnerFiltro.getSelectedItem() != null) {
                    filtrarReservas(s.toString(), spinnerFiltro.getSelectedItem().toString());
                }
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarReservas(etBuscarPlaca.getText().toString(), parent.getItemAtPosition(position).toString());
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarTodasLasReservas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT R.reservaid, R.usuarioid, R.espacioid, R.vehiculoid, R.fechareserva, R.horaentrada, R.horasalida, R.estado, R.placa, R.pago, R.ubicacion, " +
                "U.nombre AS nombreUsuario, U.apellido AS apellidoUsuario, " +
                "V.tipo AS tipoVehiculo, " +
                "E.nombre AS nombreEstacionamiento, " +
                "ES.codigo AS codigoEspacio " +
                "FROM Reservas R " +
                "LEFT JOIN Usuarios U ON R.usuarioid = U.usuarioid " +
                "LEFT JOIN Vehiculos V ON R.vehiculoid = V.vehiculoid " +
                "LEFT JOIN Espacios ES ON R.espacioid = ES.espacioid " +
                "LEFT JOIN Estacionamientos E ON ES.estacionamientoid = E.estacionamientoid " +
                "ORDER BY R.fechareserva DESC, R.horaentrada DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            reservaList.clear();
            do {
                Reserva reserva = new Reserva();
                reserva.setReservaid(cursor.getInt(cursor.getColumnIndexOrThrow("reservaid")));
                reserva.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioid")));
                reserva.setEspacioId(cursor.getInt(cursor.getColumnIndexOrThrow("espacioid")));
                reserva.setVehiculoId(cursor.getInt(cursor.getColumnIndexOrThrow("vehiculoid")));
                reserva.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fechareserva")));
                reserva.setHoraEntrada(cursor.getString(cursor.getColumnIndexOrThrow("horaentrada")));
                reserva.setHoraSalida(cursor.getString(cursor.getColumnIndexOrThrow("horasalida")));
                reserva.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                reserva.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow("placa")));
                reserva.setPago(cursor.getDouble(cursor.getColumnIndexOrThrow("pago")));
                reserva.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));
                reserva.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuario")));
                reserva.setApellidoUsuario(cursor.getString(cursor.getColumnIndexOrThrow("apellidoUsuario")));
                reserva.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow("tipoVehiculo")));
                reserva.setNombreEstacionamiento(cursor.getString(cursor.getColumnIndexOrThrow("nombreEstacionamiento")));
                reserva.setCodigoEspacio(cursor.getString(cursor.getColumnIndexOrThrow("codigoEspacio")));

                reservaList.add(reserva);
            } while (cursor.moveToNext());

            reservaAdapter.actualizarLista(reservaList);
        } else {
            Toast.makeText(this, "No hay reservas registradas.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    private void filtrarReservas(String texto, String filtroPor) {
        List<Reserva> filtradas = new ArrayList<>();
        String textoLower = texto.toLowerCase();

        for (Reserva reserva : reservaList) {
            boolean coincide = false;

            switch (filtroPor) {
                case "Todos":
                    coincide = safeContains(reserva.getPlaca(), textoLower)
                            || safeContains(reserva.getNombreUsuario(), textoLower)
                            || safeContains(reserva.getApellidoUsuario(), textoLower)
                            || safeContains(reserva.getNombreEstacionamiento(), textoLower);
                    break;

                case "Nombre":
                    String nombreCompleto = (safeText(reserva.getNombreUsuario()) + " " + safeText(reserva.getApellidoUsuario())).toLowerCase();
                    coincide = nombreCompleto.contains(textoLower);
                    break;

                case "Estacionamiento":
                    coincide = safeContains(reserva.getNombreEstacionamiento(), textoLower);
                    break;

                case "Placa":
                    coincide = safeContains(reserva.getPlaca(), textoLower);
                    break;
            }

            if (coincide) {
                filtradas.add(reserva);
            }
        }

        reservaAdapter.actualizarLista(filtradas);
    }

    private boolean safeContains(String original, String textoBuscado) {
        return original != null && original.toLowerCase().contains(textoBuscado);
    }

    private String safeText(String valor) {
        return valor != null ? valor : "";
    }
}
