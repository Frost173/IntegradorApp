// Paquete original
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
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;

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

    private TextView tvUsername;
    private FirebaseUser userFirebase;
    private FirebaseFirestore firestore;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamientos);

        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Validación inicial del usuarioId
        if (usuarioId == -1) {
            Toast.makeText(this, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Verificar si se regresó de reservas sin confirmar
        boolean reservaExitosa = getIntent().getBooleanExtra("reservaExitosa", true);
        if (!reservaExitosa) {
            Toast.makeText(this, "No se realizó ninguna reserva. Regresando al formulario...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Activity_reservas.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
            finish();
            return;
        }

        // Referencias UI
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

        dbHelper = new DBHelper(this);
        estacionamientos = dbHelper.obtenerEstacionamientos();
        adapter = new EstacionamientoAdapter(this, estacionamientos, usuarioId);
        recyclerView.setAdapter(adapter);

        // Menú lateral
        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.tv_username);
        userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (userFirebase != null) {
            String uid = userFirebase.getUid();
            firestore.collection("usuarios").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            if (nombre != null && !nombre.isEmpty()) {
                                tvUsername.setText(nombre);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "No se pudo cargar el nombre del usuario", Toast.LENGTH_SHORT).show();
                    });
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_ajustes) {
                Intent intent = new Intent(this, AjustesActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            } else if (id == R.id.nav_chats) {
                Intent intent = new Intent(this, ListaUsuariosActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrarLista(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String texto = etBuscador.getText().toString().trim();
                if (texto.isEmpty()) {
                    Toast.makeText(this, "Escribe algo para buscar", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarioActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });

        btnReservas.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_reservas.class);
            intent.putExtra("usuarioId", usuarioId); // ✅ Aquí aseguramos que se envía
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityPerfil.class);
            intent.putExtra("usuarioId", usuarioId); // ✅ Aquí también se envía
            startActivity(intent);
        });
    }
}
