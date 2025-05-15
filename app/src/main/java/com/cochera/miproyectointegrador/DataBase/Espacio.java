package com.cochera.miproyectointegrador.DataBase;

public class Espacio {
    private int espacioid;
    private int estacionamientoid;

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private String codigo;
    private String estado;

    public String getCodigo() {
        return codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstacionamientoId(int estacionamientoid) {
        this.estacionamientoid = estacionamientoid;
    }

    public int getEstacionamientoId() {
        return estacionamientoid;
    }

    public void setEspacioId(int espacioid) {
        this.espacioid = espacioid;
    }



    // Getters y Setters
}
