package com.cochera.miproyectointegrador.Login;

public interface LoginContract {
    interface View {

        //prueba
        //void showLoginSuccess(int clienteId, String nombre, String correo);
        //void showLoginError(String message);

        void goToAdminInterface();
        void goToClienteInterface(int clienteId);

        default void showLoginError(String message) {

        }
    }
    interface Presenter {
        void login(String correo, String contrasena);
    }
}

