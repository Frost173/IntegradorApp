package com.cochera.miproyectointegrador.DataBase;

public class Tarifa {
    private int idTarifa;
    private String tipoVehiculo;
    private double precio;

    public Tarifa(int idTarifa, String tipoVehiculo, double precio) {
        this.idTarifa = idTarifa;
        this.tipoVehiculo = tipoVehiculo;
        this.precio = precio;
    }

    public int getIdTarifa() { return idTarifa; }
    public String getTipoVehiculo() { return tipoVehiculo; }
    public double getPrecio() { return precio; }
}
