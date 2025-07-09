package com.cochera.miproyectointegrador;

public class Mensaje {
    private String texto;
    private String autor;
    private String emisor;
    private String receptor;
    private long timestamp;
    private String urlImagen;

    public Mensaje() {}

    public Mensaje(String texto, String autor, String emisor, String receptor, long timestamp, String urlImagen) {
        this.texto = texto;
        this.autor = autor;
        this.emisor = emisor;
        this.receptor = receptor;
        this.timestamp = timestamp;
        this.urlImagen = urlImagen;
    }

    // Getters
    public String getTexto() { return texto; }
    public String getAutor() { return autor; }
    public String getEmisor() { return emisor; }
    public String getReceptor() { return receptor; }
    public long getTimestamp() { return timestamp; }
    public String getUrlImagen() { return urlImagen; }

    // Setters
    public void setTexto(String texto) { this.texto = texto; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }
    public void setReceptor(String receptor) { this.receptor = receptor; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}

