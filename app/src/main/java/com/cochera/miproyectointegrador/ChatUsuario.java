package com.cochera.miproyectointegrador;

import com.cochera.miproyectointegrador.DataBase.Usuario;

public class ChatUsuario {
    private Usuario usuario;
    private String ultimoMensaje;
    private String hora;

    public ChatUsuario() {}

    public ChatUsuario(Usuario usuario, String ultimoMensaje, String hora) {
        this.usuario = usuario;
        this.ultimoMensaje = ultimoMensaje;
        this.hora = hora;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getUltimoMensaje() { return ultimoMensaje; }
    public void setUltimoMensaje(String ultimoMensaje) { this.ultimoMensaje = ultimoMensaje; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}

