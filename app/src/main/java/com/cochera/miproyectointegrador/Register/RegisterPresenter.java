package com.cochera.miproyectointegrador.Register;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterPresenter implements RegisterContract.Presenter {

    private static final String TAG = "RegisterPresenter";

    private RegisterContract.View view;
    private DBHelper dbHelper;
    private Context context;

    public RegisterPresenter(RegisterContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbHelper = new DBHelper(this.context);
    }

    @Override
    public void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String celular, int perfilid) {
        if (view == null) {
            Log.e(TAG, "La vista es null en registrarUsuario");
            return;
        }

        Toast.makeText(context, "Intentando registrar...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Datos recibidos: Nombre=" + nombre + ", Correo=" + correo);

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || celular.isEmpty()) {
            view.showRegisterError("Todos los campos son obligatorios");
            return;
        }

        if (!correo.endsWith("@gmail.com") || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            view.showRegisterError("Solo se permiten correos Gmail válidos");
            return;
        }

        if (!Pattern.matches("^[0-9]{9}$", celular)) {
            view.showRegisterError("Número de celular inválido. Debe tener 9 dígitos.");
            return;
        }

        if (!esContrasenaSegura(contrasena)) {
            view.showRegisterError("La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula, un número y un símbolo.");
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verifyTask -> {
                                        if (verifyTask.isSuccessful()) {
                                            Log.d(TAG, "Correo de verificación enviado");
                                        } else {
                                            Log.e(TAG, "Fallo al enviar verificación: " + verifyTask.getException().getMessage());
                                        }
                                    });

                            long usuarioId;
                            try {
                                usuarioId = dbHelper.insertarUsuarioYRetornarID(nombre, apellido, correo, contrasena, celular, perfilid);
                            } catch (Exception e) {
                                Log.e(TAG, "Error en SQLite: " + e.getMessage());
                                view.showRegisterError("Error interno al registrar");
                                return;
                            }

                            if (usuarioId != -1) {
                                String perfilTexto = (perfilid == 1) ? "admin" : "usuario";
                                subirUsuarioAFirebase(nombre, apellido, correo, celular, perfilTexto, usuarioId, user.getUid());
                            } else {
                                view.showRegisterError("El correo ya está registrado localmente");
                            }

                            view.showRegisterSuccess();

                        } else {
                            view.showRegisterError("No se pudo obtener el usuario actual");
                        }

                    } else {
                        view.showRegisterError("Error Firebase Auth: " + task.getException().getMessage());
                    }
                });
    }

    private void subirUsuarioAFirebase(String nombre, String apellido, String correo, String celular, String perfilTexto, long usuarioId, String uid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Subir a Firestore
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("idUsuario", usuarioId);
        userMap.put("uid", uid);
        userMap.put("nombre", nombre);
        userMap.put("apellido", apellido);
        userMap.put("correo", correo);
        userMap.put("celular", celular);
        userMap.put("perfil", perfilTexto);  // ✅ SOLO perfil como texto

        firestore.collection("usuarios").document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Usuario subido a Firestore"))
                .addOnFailureListener(e -> Log.e(TAG, "Error Firestore: " + e.getMessage()));

        // Subir también a Realtime Database si lo necesitas
        Usuario usuarioRTDB = new Usuario();
        usuarioRTDB.setId((int) usuarioId);
        usuarioRTDB.setUid(uid);
        usuarioRTDB.setNombre(nombre);
        usuarioRTDB.setApellido(apellido);
        usuarioRTDB.setCorreo(correo);
        usuarioRTDB.setPerfil(perfilTexto);  // ✅ perfil como texto
        usuarioRTDB.setEstado("activo");

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .setValue(usuarioRTDB)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Usuario subido a Realtime Database"))
                .addOnFailureListener(e -> Log.e(TAG, "Error RealtimeDB: " + e.getMessage()));
    }

    private boolean esContrasenaSegura(String contrasena) {
        if (contrasena.length() < 6) return false;
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
        return pattern.matcher(contrasena).matches();
    }
}
