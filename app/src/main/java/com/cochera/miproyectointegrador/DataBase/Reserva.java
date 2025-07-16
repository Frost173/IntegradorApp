package com.cochera.miproyectointegrador.DataBase;

public class Reserva {

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
    private double pagoHora; // Esto es para cálculo estimado (si usas tarifa por hora)
    private double pago;     // Esto es el total registrado en la BD
    private String ubicacion;

    // Estado y descripción
    private String estado;
    private String nombreEstacionamiento;
    private String codigoEspacio;

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
    public double getPago() { return pago; }
    public String getUbicacion() { return ubicacion; }
    public String getEstado() { return estado; }
    public String getNombreEstacionamiento() { return nombreEstacionamiento; }
    public String getCodigoEspacio() { return codigoEspacio; }

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
    public void setNombreEstacionamiento(String nombreEstacionamiento) { this.nombreEstacionamiento = nombreEstacionamiento; }
    public void setCodigoEspacio(String codigoEspacio) { this.codigoEspacio = codigoEspacio; }

    // Alias opcionales (por compatibilidad con otras clases o adaptadores)
    public void setId(int id) {
        this.reservaid = id;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fecha = fechaReserva;
    }

    // Cálculo de pago estimado basado en diferencia de horas
    public double getPagoCalculado() {
        try {
            String[] entrada = horaEntrada.split(":");
            String[] salida = horaSalida.split(":");

            int hEntrada = Integer.parseInt(entrada[0]);
            int mEntrada = Integer.parseInt(entrada[1]);

            int hSalida = Integer.parseInt(salida[0]);
            int mSalida = Integer.parseInt(salida[1]);

            // Duración en horas con decimales
            double duracion = (hSalida + mSalida / 60.0) - (hEntrada + mEntrada / 60.0);

            // Asegurarse que no sea negativo
            return pagoHora * Math.max(duracion, 0);
        } catch (Exception e) {
            return 0;
        }
    }
}
