package com.cochera.miproyectointegrador.Login;

public interface LoginContract {
    interface View {
<<<<<<< HEAD
        //void showLoginSuccess(int clienteId, String nombre, String correo);
        //void showLoginError(String message);

        void goToAdminInterface();
        void goToClienteInterface(int clienteId);
        void showLoginError(String message);
    }
    interface Presenter {
        void login(String correo, String contrasena);
=======
        void showLoginSuccess(int clienteId, String nombre, String correo);
        void showLoginError(String message);
    }
    interface Presenter {
        void login(String correo, String contraseña);
>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4
    }
}

