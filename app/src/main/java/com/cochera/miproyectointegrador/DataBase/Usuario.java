    package com.cochera.miproyectointegrador.DataBase;

    public class Usuario {
        private int id;
        private String uid;
        private String nombre;
        private String apellido;
        private String correo;
        private String contrasena;
        private String celular;
        private String perfil;
        private String estado = "Activo";
        private int edad = 18;

        // 🔹 Constructor vacío (obligatorio para Firebase)
        public Usuario() {}

        // 🔹 Constructor completo SIN ID
        public Usuario(String uid, String nombre, String apellido, String correo,
                       String contrasena, String celular, String perfil, String estado, int edad) {
            this.uid = uid;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.contrasena = contrasena;
            this.celular = celular;
            this.perfil = perfil;
            this.estado = estado;
            this.edad = edad;
        }

        // 🔹 Constructor usado en login local (SQLite)
        public Usuario(int id, String nombre, String apellido, String correo, String perfil) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.perfil = perfil;
        }

        // 🔹 Constructor completo CON ID (opcional si lo necesitas)
        public Usuario(int id, String uid, String nombre, String apellido, String correo,
                       String contrasena, String celular, String perfil, String estado, int edad) {
            this.id = id;
            this.uid = uid;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.contrasena = contrasena;
            this.celular = celular;
            this.perfil = perfil;
            this.estado = estado;
            this.edad = edad;
        }

        // 🔸 Getters
        public int getId() { return id; }
        public String getUid() { return uid; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getCorreo() { return correo; }
        public String getContrasena() { return contrasena; }
        public String getCelular() { return celular; }
        public String getPerfil() { return perfil; }
        public String getEstado() { return estado; }
        public int getEdad() { return edad; }

        // 🔸 Setters
        public void setId(int id) { this.id = id; }
        public void setUid(String uid) { this.uid = uid; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public void setCorreo(String correo) { this.correo = correo; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        public void setCelular(String celular) { this.celular = celular; }
        public void setPerfil(String perfil) { this.perfil = perfil; }
        public void setEstado(String estado) { this.estado = estado; }
        public void setEdad(int edad) { this.edad = edad; }
    }
