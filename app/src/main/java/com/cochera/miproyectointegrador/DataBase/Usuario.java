package com.cochera.miproyectointegrador.DataBase;

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

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getPerfil() { return perfil; }
}
