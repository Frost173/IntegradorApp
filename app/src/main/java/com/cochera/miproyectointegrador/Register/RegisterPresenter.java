package com.cochera.miproyectointegrador.Register;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class RegisterPresenter implements RegisterContract.Presenter {

    private static final String TAG = "RegisterPresenter"; // Tag para Logs

    private RegisterContract.View view;
    private DBHelper dbHelper;
    private Context context; // Guardar el contexto si se va a usar para más Toasts o recursos

    public RegisterPresenter(RegisterContract.View view, Context context) {
        this.view = view;
        this.context = context; // Usar el contexto de la aplicación para DBHelper es generalmente más seguro
        this.dbHelper = new DBHelper(this.context);
    }

    @Override
    public void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String celular, int perfilid) {
        if (view == null) {
            Log.e(TAG, "La vista es null en registrarUsuario");
            return;
        }

        Toast.makeText(context, "Intentando registrar...", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Datos recibidos para registrar: Nombre=" + nombre + ", Correo=" + correo);

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || celular.isEmpty()) {
            view.showRegisterError("Todos los campos son obligatorios");
            return;
        }

        boolean registrado = false;
        try {
            registrado = dbHelper.insertarUsuario(nombre, apellido, correo, contrasena, celular, perfilid);
        } catch (Exception e) {
            Log.e(TAG, "Error al llamar a dbHelper.insertarUsuario: " + e.getMessage());
            e.printStackTrace();
            view.showRegisterError("Error interno al registrar usuario");
            return;
        }

        if (registrado) {
            view.showRegisterSuccess();
        } else {
            view.showRegisterError("El correo ya está registrado o hubo un error");
        }
    }

}