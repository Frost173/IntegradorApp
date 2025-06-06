package com.cochera.miproyectointegrador.DataBase;

public class Reserva {
    public String placa;
    private int reservaid;
    private int usuarioId;
    private int espacioId;
    private int vehiculoId;
    private String fecha;
    private String horaEntrada;
    private String horaSalida;
    private double pagoHora;
    private String ubicacion;
    private String tipoVehiculo;
    private String estado;

    // Constructor vacío
    public Reserva() {}

    // Getters
    public int getReservaid() { return reservaid; }
    public int getUsuarioId() { return usuarioId; }
    public int getEspacioId() { return espacioId; }
    public int getVehiculoId() { return vehiculoId; }
    public String getFecha() { return fecha; }
    public String getHoraEntrada() { return horaEntrada; }
    public String getHoraSalida() { return horaSalida; }
    public double getPagoHora() { return pagoHora; }
    public String getUbicacion() { return ubicacion; }
    public String getPlaca() { return placa; }
    public String getTipoVehiculo() { return tipoVehiculo; }
    public String getEstado() { return estado; }

    // Setters
    public void setReservaid(int reservaid) { this.reservaid = reservaid; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setEspacioId(int espacioId) { this.espacioId = espacioId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHoraEntrada(String horaEntrada) { this.horaEntrada = horaEntrada; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }
    public void setPagoHora(double pagoHora) { this.pagoHora = pagoHora; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setPlaca(String placa) { this.placa = placa; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
    public void setEstado(String estado) { this.estado = estado; }
}
