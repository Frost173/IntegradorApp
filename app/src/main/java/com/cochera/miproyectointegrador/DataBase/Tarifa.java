package com.cochera.miproyectointegrador.DataBase;

public class Tarifa {
    private int tarifaid;
    private int estacionamientoid;
    private String tipoVehiculo;
    private double precio;

    // Constructor vacío
    public Tarifa() {
    }

    // Constructor con parámetros
    public Tarifa(int tarifaid, int estacionamientoid, String tipoVehiculo, double precio) {
        this.tarifaid = tarifaid;
        this.estacionamientoid = estacionamientoid;
        this.tipoVehiculo = tipoVehiculo;
        this.precio = precio;
    }

    public int getTarifaid() {
        return tarifaid;
    }

    public void setTarifaid(int tarifaid) {
        this.tarifaid = tarifaid;
    }

    public int getEstacionamientoid() {
        return estacionamientoid;
    }

    public void setEstacionamientoid(int estacionamientoid) {
        this.estacionamientoid = estacionamientoid;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
