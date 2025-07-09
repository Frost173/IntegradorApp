package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnFlecha, btnSend;
    private EditText etMessage;
    private RecyclerView rvMessages;
    private TextView tvTitulo;

    private MessageAdapter adapter;
    private List<Mensaje> listaMensajes;

    private FirebaseUser userActual;
    private DatabaseReference mensajesRef;

    private String uidDestino;
    private String nombreDestino;
    private String chatId;
    private String nombreEmisor = "Yo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_personal);

        uidDestino = getIntent().getStringExtra("uidDestino");
        nombreDestino = getIntent().getStringExtra("nombreDestino");

        userActual = FirebaseAuth.getInstance().getCurrentUser();
        if (userActual == null || uidDestino == null) {
            Toast.makeText(this, "Error al cargar el chat", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtener nombre del emisor desde Firestore
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(userActual.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        nombreEmisor = snapshot.getString("nombre");
                    }
                });

        inicializarVistas();

        // Configurar RecyclerView
        listaMensajes = new ArrayList<>();
        adapter = new MessageAdapter(listaMensajes, userActual.getUid());
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        // Crear chatId único (ordenado por UID)
        chatId = getChatId(userActual.getUid(), uidDestino);

        // Referencia de mensajes
        mensajesRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(chatId)
                .child("mensajes");

        // Escuchar nuevos mensajes
        mensajesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje mensaje = snapshot.getValue(Mensaje.class);
                if (mensaje != null) {
                    listaMensajes.add(mensaje);
                    adapter.notifyItemInserted(listaMensajes.size() - 1);
                    rvMessages.scrollToPosition(listaMensajes.size() - 1);
                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnSend.setOnClickListener(this::onClick);
    }

    private void inicializarVistas() {
        btnFlecha = findViewById(R.id.btnFlecha);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        rvMessages = findViewById(R.id.rvMessages);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText(nombreDestino);

        btnFlecha.setOnClickListener(v -> finish());
    }

    private void onClick(View v) {
        String texto = etMessage.getText().toString().trim();
        if (texto.isEmpty()) return;

        FirebaseFirestore.getInstance()
                .collection("Usuarios")
                .document(userActual.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    String nombre = snapshot.getString("nombre");

                    Mensaje mensaje = new Mensaje(
                            texto,
                            nombre != null ? nombre : "Desconocido",
                            userActual.getUid(),
                            uidDestino,
                            System.currentTimeMillis()
                    );

                    // Enviar mensaje al chat
                    mensajesRef.push().setValue(mensaje);

                    // Limpiar campo de texto
                    etMessage.setText("");

                    // Registrar relación del chat
                    DatabaseReference refUsuariosChats = FirebaseDatabase.getInstance().getReference("usuariosChats");
                    refUsuariosChats.child(userActual.getUid()).child(uidDestino).setValue(true);
                    refUsuariosChats.child(uidDestino).child(userActual.getUid()).setValue(true);
                });
    }

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}
