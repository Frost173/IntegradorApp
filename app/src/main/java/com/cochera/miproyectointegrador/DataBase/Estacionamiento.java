package com.cochera.miproyectointegrador.DataBase;

public class Estacionamiento {
    private int estacionamientoId;
    private String nombre;
    private String direccion;
    private int propietarioId;
    private String imagenUrl;


    public Estacionamiento(int estacionamientoId, String nombre, String direccion, int propietarioId, String imagenUrl) {
        this.estacionamientoId = estacionamientoId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.propietarioId = propietarioId;
        this.imagenUrl = imagenUrl;
    }

    // Constructor sin imagen (opcional, para compatibilidad)
    public Estacionamiento(int estacionamientoId, String nombre, String direccion, int propietarioId) {
        this.estacionamientoId = estacionamientoId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.propietarioId = propietarioId;
    }

    // Getters y Setters
    public int getEstacionamientoId() { return estacionamientoId; }
    public void setEstacionamientoId(int estacionamientoId) { this.estacionamientoId = estacionamientoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getPropietarioId() { return propietarioId; }
    public void setPropietarioId(int propietarioId) { this.propietarioId = propietarioId; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
