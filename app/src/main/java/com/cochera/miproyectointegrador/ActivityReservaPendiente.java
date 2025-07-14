package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;
import com.cochera.miproyectointegrador.ReservaPendienteAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityReservaPendiente extends AppCompatActivity {

    private RecyclerView recyclerPendientes;
    private ReservaPendienteAdapter adapter;
    private List<Reserva> reservasPendientes;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_pendiente);

        // Inicializar RecyclerView
        recyclerPendientes = findViewById(R.id.recyclerPendientes);
        recyclerPendientes.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar base de datos y lista
        dbHelper = new DBHelper(this);
        reservasPendientes = new ArrayList<>();

        // Adaptador
        adapter = new ReservaPendienteAdapter(reservasPendientes, dbHelper, this);
        recyclerPendientes.setAdapter(adapter);

        // Cargar datos
        cargarReservasPendientes();
    }

    private void cargarReservasPendientes() {
        reservasPendientes.clear();
        reservasPendientes.addAll(dbHelper.obtenerReservasPorEstado("Pendiente")); // Asegúrate que este método existe
        adapter.notifyDataSetChanged();

        if (reservasPendientes.isEmpty()) {
            Toast.makeText(this, "No hay reservas pendientes.", Toast.LENGTH_SHORT).show();
        }
    }
}
