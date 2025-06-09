package com.cochera.miproyectointegrador.DataBase;

public class Reserva {
    // Datos de relación
    private int reservaid;
    private int usuarioId;
    private int espacioId;
    private int vehiculoId;

    // Datos del usuario
    private String nombreUsuario;
    private String apellidoUsuario;

    // Datos del vehículo
    private String placa;
    private String tipoVehiculo;

    // Detalles de la reserva
    private String fecha;
    private String horaEntrada;
    private String horaSalida;

    // Información de pago y ubicación
    private double pagoHora;
    private double pago; // este es el que leeremos de la BD
    private String ubicacion;

    // Estado de la reserva (ej: "activo", "cancelado", etc.)
    private String estado;

    // Constructor vacío
    public Reserva() {}

    // Getters
    public int getReservaid() { return reservaid; }
    public int getUsuarioId() { return usuarioId; }
    public int getEspacioId() { return espacioId; }
    public int getVehiculoId() { return vehiculoId; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getApellidoUsuario() { return apellidoUsuario; }
    public String getPlaca() { return placa; }
    public String getTipoVehiculo() { return tipoVehiculo; }
    public String getFecha() { return fecha; }
    public String getHoraEntrada() { return horaEntrada; }
    public String getHoraSalida() { return horaSalida; }
    public double getPagoHora() { return pagoHora; }
    public double getPago() { return pago; }  // <--- ahora usamos esto directamente
    public String getUbicacion() { return ubicacion; }
    public String getEstado() { return estado; }

    // Setters
    public void setReservaid(int reservaid) { this.reservaid = reservaid; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setEspacioId(int espacioId) { this.espacioId = espacioId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setApellidoUsuario(String apellidoUsuario) { this.apellidoUsuario = apellidoUsuario; }
    public void setPlaca(String placa) { this.placa = placa; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHoraEntrada(String horaEntrada) { this.horaEntrada = horaEntrada; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }
    public void setPagoHora(double pagoHora) { this.pagoHora = pagoHora; }
    public void setPago(double pago) { this.pago = pago; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setEstado(String estado) { this.estado = estado; }

    // Alias methods
    public void setId(int id) {
        this.reservaid = id;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fecha = fechaReserva;
    }

    //  Eliminado el método que calculaba el pago (confundía si los datos no estaban completos)
    // Si lo necesitas, puedes renombrarlo como getPagoCalculado() y usarlo opcionalmente

    public double getPagoCalculado() {
        try {
            String[] entrada = horaEntrada.split(":");
            String[] salida = horaSalida.split(":");
            int hEntrada = Integer.parseInt(entrada[0]);
            int mEntrada = Integer.parseInt(entrada[1]);
            int hSalida = Integer.parseInt(salida[0]);
            int mSalida = Integer.parseInt(salida[1]);

            double duracion = (hSalida + mSalida / 60.0) - (hEntrada + mEntrada / 60.0);
            return pagoHora * (duracion > 0 ? duracion : 0);
        } catch (Exception e) {
            return 0;
        }
    }


}

