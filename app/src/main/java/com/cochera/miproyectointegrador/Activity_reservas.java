package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.util.ArrayList;

//public class Activity_reservas extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    ReservaAdapter reservaAdapter;
//    ArrayList<Reserva> listaReservas;
//    DBHelper dbHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_reservas);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        recyclerView = findViewById(R.id.recyclerReservas);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        dbHelper = new DBHelper(this);
//        listaReservas = dbHelper.obtenerReservas(); // Método que debes crear en DBHelper
//        reservaAdapter = new ReservaAdapter(listaReservas, this);
//
//        recyclerView.setAdapter(reservaAdapter);
//    }
//}

public class Activity_reservas extends AppCompatActivity {

    private RecyclerView recyclerReservas;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas); // Asegúrate de que el XML se llame así

        // Inicializar RecyclerView
        recyclerReservas = findViewById(R.id.recyclerReservas);

        // Aquí podrías agregar un Adapter más adelante
        // recyclerReservas.setLayoutManager(new LinearLayoutManager(this));
        // recyclerReservas.setAdapter(new TuAdaptador(listaDeDatos));
    }
}