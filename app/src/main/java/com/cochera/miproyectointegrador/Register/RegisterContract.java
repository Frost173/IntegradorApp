package com.cochera.miproyectointegrador.Register;

public interface RegisterContract {
    interface View {
        void showRegisterSuccess();
        void showRegisterError(String msg);
        void showSuccess(String message);
        void showError(String message);
    }

    interface Presenter {
        // Usamos "perfil" como String, no int
        void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String celular, String perfil);
    }
}
