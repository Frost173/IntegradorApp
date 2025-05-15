package com.cochera.miproyectointegrador.DataBase;

public class Reserva {
    public int reservaid;
    public String horaEntrada;
    public String horaSalida;
    public String fecha;
    public double pagoHora;
    public String ubicacion;
    public String placa;

    public Reserva(int reservaid, String horaEntrada, String horaSalida, String fecha, double pagoHora, String ubicacion, String placa) {
        this.reservaid = reservaid;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fecha = fecha;
        this.pagoHora = pagoHora;
        this.ubicacion = ubicacion;
        this.placa = placa;
    }
}
