package com.cochera.miproyectointegrador.Register;

public interface RegisterContract {
    interface View {
        void showRegisterSuccess();

        void showRegisterError(String msg);

        void showSuccess(String message);
        void showError(String message);
    }

    interface Presenter {
        void registerUser(String nombre, String correo, String contrasena, String celular);
    }
}

