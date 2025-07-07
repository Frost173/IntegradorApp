package com.cochera.miproyectointegrador.Register;

import android.content.Context;
import android.util.Log;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View view;
    private final Context context;

    public RegisterPresenter(RegisterContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String celular, String perfil) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user == null) {
                            view.showRegisterError("Error inesperado: usuario nulo");
                            return;
                        }

                        String uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Datos para Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("nombre", nombre);
                        userData.put("apellido", apellido);
                        userData.put("correo", correo);
                        userData.put("celular", celular);
                        userData.put("perfil", perfil);
                        userData.put("uid", uid);

                        // Guardar en Firestore
                        db.collection("Usuarios").document(uid).set(userData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Guardar en SQLite
                                        DBHelper dbHelper = new DBHelper(context);

                                        Log.d("SQLITE_DEBUG", "Intentando guardar en SQLite...");
                                        boolean exito = dbHelper.insertarUsuario(nombre, apellido, correo, celular, perfil, uid, contrasena);
                                        Log.d("SQLITE_DEBUG", "Guardado en SQLite: " + exito);

                                        // Enviar correo de verificación
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(emailTask -> {
                                                    if (emailTask.isSuccessful()) {
                                                        view.showRegisterSuccess();
                                                    } else {
                                                        view.showRegisterError("No se pudo enviar el correo de verificación.");
                                                    }
                                                });
                                    } else {
                                        String errorMsg = (task1.getException() != null)
                                                ? task1.getException().getMessage()
                                                : "Error desconocido al guardar en Firestore";
                                        Log.e("Firestore", "Error al guardar en Firestore", task1.getException());
                                        view.showRegisterError("Error al guardar datos: " + errorMsg);
                                    }
                                });

                    } else {
                        String errorMsg = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Error desconocido en FirebaseAuth";
                        Log.e("FirebaseAuth", "Error de registro", task.getException());

                        if (errorMsg.contains("email address is already in use")) {
                            view.showRegisterError("Este correo ya está registrado.");
                        } else {
                            view.showRegisterError("Error al registrar: " + errorMsg);
                        }
                    }
                });
    }
}
