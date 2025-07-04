package com.cochera.miproyectointegrador;

public class Mensaje {
    private String texto;
    private String autor;
    private String emisor;
    private String receptor;
    private long timestamp;

    public Mensaje() {}

    public Mensaje(String texto, String autor, String emisor, String receptor, long timestamp) {
        this.texto = texto;
        this.autor = autor;
        this.emisor = emisor;
        this.receptor = receptor;
        this.timestamp = timestamp;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEmisor() { return emisor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }

    public String getReceptor() { return receptor; }
    public void setReceptor(String receptor) { this.receptor = receptor; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
