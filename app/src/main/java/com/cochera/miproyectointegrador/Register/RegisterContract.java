package com.cochera.miproyectointegrador.Register;

public interface RegisterContract {
    interface View {
        void showRegisterSuccess();
        void showRegisterError(String msg);
        void showSuccess(String message);
        void showError(String message);
    }

    interface Presenter {
        // Aquí debe llamarse registrarUsuario y con 6 parámetros
        void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String celular, int perfilid);
    }
}
