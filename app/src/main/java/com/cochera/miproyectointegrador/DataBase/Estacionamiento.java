package com.cochera.miproyectointegrador.DataBase;

public class Estacionamiento {
        private int estacionamientoId;
        private String nombre;
        private String direccion;
        private int propietarioId;

        public Estacionamiento(int estacionamientoId, String nombre, String direccion, int propietarioId) {
            this.estacionamientoId = estacionamientoId;
            this.nombre = nombre;
            this.direccion = direccion;
            this.propietarioId = propietarioId;
        }

        public int getEstacionamientoId() {
            return estacionamientoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setEstacionamientoId(int estacionamientoId) {
            this.estacionamientoId = estacionamientoId;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public void setPropietarioId(int propietarioId) {
            this.propietarioId = propietarioId;
        }

        public String getDireccion() {
                return direccion;
            }

        public int getPropietarioId() {
            return propietarioId;
        }
}
