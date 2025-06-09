package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Estacionamiento;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class Activity_estacionamientos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;
    private EditText etBuscador;
    private ImageButton btnReservas, btnHome, btnCalendario, btnPerfil;
    private RecyclerView recyclerView;
    private EstacionamientoAdapter adapter;
    private DBHelper dbHelper;
    private List<Estacionamiento> estacionamientos;
    private int usuarioId;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamientos);

        // Obtener usuarioId
        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menuIcon);
        etBuscador = findViewById(R.id.etBuscador);
        btnReservas = findViewById(R.id.btnReservas);
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);
        recyclerView = findViewById(R.id.recyclerEstacionamientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos
        dbHelper = new DBHelper(this);
        estacionamientos = dbHelper.obtenerEstacionamientos();

        // Configurar adapter con lista completa
        adapter = new EstacionamientoAdapter(this, estacionamientos, usuarioId);
        recyclerView.setAdapter(adapter);

        // Drawer - abrir y cerrar
        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Navegación en el menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_ajustes) {
                Intent intent = new Intent(this, AjustesActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            } else if (id == R.id.nav_chats) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Filtrado en tiempo real con TextWatcher
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrarLista(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Opcional: onEditorActionListener para buscar con teclado
        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String texto = etBuscador.getText().toString().trim();
                if (texto.isEmpty()) {
                    Toast.makeText(this, "Escribe para buscar", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        // Botones inferiores
        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarioActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });
        btnReservas.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_reservas.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v -> Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );
        btnPerfil.setOnClickListener(v -> {
            if (usuarioId != -1) {
                Intent intent = new Intent(Activity_estacionamientos.this, ActivityPerfil.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            } else {
                Toast.makeText(Activity_estacionamientos.this, "Usuario no válido", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
