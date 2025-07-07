package com.cochera.miproyectointegrador.DataBase;

public class Espacio {
    private int espacioid;
    private int estacionamientoid;
    private String codigo;
    private String estado;
    private String ubicacion; // NUEVO CAMPO

    // Constructor vacío
    public Espacio() {}

    // Constructor completo
    public Espacio(int espacioid, int estacionamientoid, String codigo, String estado) {
        this.espacioid = espacioid;
        this.estacionamientoid = estacionamientoid;
        this.codigo = codigo;
        this.estado = estado;
        this.ubicacion = estacionamientoid + codigo; // Generamos la ubicación, ej: "1A1"
    }

    // Getters
    public int getEspacioId() {
        return espacioid;
    }

    public int getEstacionamientoId() {
        return estacionamientoid;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstado() {
        return estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    // Setters
    public void setEspacioId(int espacioid) {
        this.espacioid = espacioid;
    }

    public void setEstacionamientoId(int estacionamientoid) {
        this.estacionamientoid = estacionamientoid;
        this.ubicacion = estacionamientoid + codigo; // Recalcula ubicación
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.ubicacion = estacionamientoid + codigo; // Recalcula ubicación
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
