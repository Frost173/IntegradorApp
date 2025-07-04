package com.cochera.miproyectointegrador.DataBase;

public class Usuario {
    private int id;
    private String uid;        // ID de Firebase Auth
    private String nombre;
    private String apellido;
    private String correo;
    private String perfil;
    private String estado = "desconocido"; // valor por defecto
    private int edad = 0;                  // valor por defecto

    public Usuario() {
        // Constructor vacío necesario para Firebase
    }

    // Constructor completo
    public Usuario(int id, String uid, String nombre, String apellido, String correo, String perfil, String estado) {
        this.id = id;
        this.uid = uid;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.perfil = perfil;
        this.estado = estado;
    }

    // ✅ Constructor usado en tu error (5 parámetros)
    public Usuario(int id, String nombre, String apellido, String correo, String perfil) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.perfil = perfil;
    }

    // Getters
    public int getId() { return id; }
    public String getUid() { return uid; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getPerfil() { return perfil; }
    public String getEstado() { return estado; }
    public int getEdad() { return edad; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUid(String uid) { this.uid = uid; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setEdad(int edad) { this.edad = edad; }
}
