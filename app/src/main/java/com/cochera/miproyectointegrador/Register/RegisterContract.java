package com.cochera.miproyectointegrador.Register;

public interface RegisterContract {
    interface View {
        void showRegisterSuccess();
        void showRegisterError(String msg);
    }
    interface Presenter {
        void register(String nombre, String correo, String dni, String celular);
    }
}
