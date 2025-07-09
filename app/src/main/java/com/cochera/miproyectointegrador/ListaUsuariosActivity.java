package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.firestore.*;

import java.util.*;

public class ListaUsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterChatUsuario adapter;
    private final List<ChatUsuario> listaChats = new ArrayList<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseDatabase realtimeDb = FirebaseDatabase.getInstance();
    private FirebaseUser userActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        recyclerView = findViewById(R.id.recyclerUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterChatUsuario(this, listaChats);
        recyclerView.setAdapter(adapter);

        userActual = FirebaseAuth.getInstance().getCurrentUser();
        if (userActual != null) {
            cargarTodosLosUsuarios(userActual.getUid());
        }
    }

    private void cargarTodosLosUsuarios(String miUid) {
        firestore.collection("Usuarios").get().addOnSuccessListener(snapshot -> {
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                String uid = doc.getId();
                if (uid.equals(miUid)) continue; // no incluirte a ti mismo

                Usuario usuario = new Usuario();
                usuario.setUid(uid);
                usuario.setNombre(doc.getString("nombre"));
                usuario.setApellido(doc.getString("apellido"));
                usuario.setCorreo(doc.getString("correo"));
                usuario.setCelular(doc.getString("celular"));
                usuario.setPerfil(doc.getString("perfil"));

                if (usuario == null) continue;
                usuario.setUid(uid);

                obtenerUltimoMensaje(usuario, miUid);
            }
        });
    }

    private void obtenerUltimoMensaje(Usuario usuario, String miUid) {
        String otroUid = usuario.getUid();
        String chatId = getChatId(miUid, otroUid);

        DatabaseReference mensajesRef = realtimeDb.getReference("chats").child(chatId).child("mensajes");

        mensajesRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String texto = "Sin mensajes";
                String hora = "--:--";

                for (DataSnapshot mensajeSnap : snapshot.getChildren()) {
                    Mensaje mensaje = mensajeSnap.getValue(Mensaje.class);
                    if (mensaje != null) {
                        texto = mensaje.getTexto();
                        hora = obtenerHora(mensaje.getTimestamp());
                    }
                }

                ChatUsuario chat = new ChatUsuario(usuario, texto, hora);
                listaChats.add(chat);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de error si falla la lectura
            }
        });
    }

    private String obtenerHora(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("hh:mm a", cal).toString();
    }

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}
