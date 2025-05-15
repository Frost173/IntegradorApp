package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;

//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class activity_control_espacios extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_control_espacios);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//
//    }
//}
public class activity_control_espacios extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;
    EspacioAdapter adapter;
    List<Espacio> listaEspacios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(activity_control_espacios.this);
        setContentView(R.layout.activity_control_espacios);  // Usamos el nuevo layout

        // EdgeToEdge ajustes de padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asumiendo que en tu layout tienes un RecyclerView con este id
        recyclerView = findViewById(R.id.recyclerEspacios);
        dbHelper = new DBHelper(this);

        int estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);

        if (estacionamientoId != -1) {
            listaEspacios = dbHelper.obtenerEspaciosPorEstacionamiento(estacionamientoId);
            adapter = new EspacioAdapter(this, listaEspacios);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Error al recibir el ID del estacionamiento", Toast.LENGTH_SHORT).show();
        }
    }
}