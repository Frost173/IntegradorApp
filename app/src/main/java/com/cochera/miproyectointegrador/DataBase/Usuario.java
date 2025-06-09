package com.cochera.miproyectointegrador.DataBase;

import com.cochera.miproyectointegrador.DataBase.Usuario;


public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String perfil;

    public Usuario(int id, String nombre, String apellido, String correo, String perfil) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.perfil = perfil;
    }

    public Usuario() {

    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getPerfil() { return perfil; }

    public void setId(int id) {
    }

    public void setNombre(String nombre) {
    }

    public void setApellido(String apellido) {
    }

    public void setEdad(int edad) {
    }

    public void setCorreo(String string) {
    }

    public void setPerfil(String string) {
    }
}