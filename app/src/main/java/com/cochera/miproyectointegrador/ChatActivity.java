package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class ChatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private ImageButton btnFlecha, btnSend, btnCamera;
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

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String API_KEY_IMGBB = "88982f8b28b0aea75a6f1108eba54037";

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

        inicializarVistas();

        listaMensajes = new ArrayList<>();
        adapter = new MessageAdapter(listaMensajes, userActual.getUid());
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        chatId = getChatId(userActual.getUid(), uidDestino);
        mensajesRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(chatId)
                .child("mensajes");

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

        btnSend.setOnClickListener(this::enviarTexto);
        btnCamera.setOnClickListener(v -> abrirGaleria());
    }

    private void inicializarVistas() {
        btnFlecha = findViewById(R.id.btnFlecha);
        btnSend = findViewById(R.id.btnSend);
        btnCamera = findViewById(R.id.btnAbrirCamara);
        etMessage = findViewById(R.id.etMessage);
        rvMessages = findViewById(R.id.rvMessages);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText(nombreDestino);
        btnFlecha.setOnClickListener(v -> finish());
    }

    private void enviarTexto(View v) {
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
                            System.currentTimeMillis(),
                            null // urlImagen
                    );

                    mensajesRef.push().setValue(mensaje);
                    etMessage.setText("");

                    DatabaseReference refUsuariosChats = FirebaseDatabase.getInstance().getReference("usuariosChats");
                    refUsuariosChats.child(userActual.getUid()).child(uidDestino).setValue(true);
                    refUsuariosChats.child(uidDestino).child(userActual.getUid()).setValue(true);
                });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            subirImagenAImgBB(data.getData());
        }
    }

    private void subirImagenAImgBB(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            RequestBody formBody = new FormBody.Builder()
                    .add("key", API_KEY_IMGBB)
                    .add("image", base64Image)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(formBody)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            String urlImagen = json.getJSONObject("data").getString("url");

                            runOnUiThread(() -> enviarMensajeConImagen(urlImagen));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarMensajeConImagen(String urlImagen) {
        FirebaseFirestore.getInstance()
                .collection("Usuarios")
                .document(userActual.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    String nombre = snapshot.getString("nombre");

                    Mensaje mensaje = new Mensaje(
                            "📷 Imagen",
                            nombre != null ? nombre : "Desconocido",
                            userActual.getUid(),
                            uidDestino,
                            System.currentTimeMillis(),
                            urlImagen
                    );

                    mensajesRef.push().setValue(mensaje);
                });
    }

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}
