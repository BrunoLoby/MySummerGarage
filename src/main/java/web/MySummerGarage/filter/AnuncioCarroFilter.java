package web.MySummerGarage.filter;

import web.MySummerGarage.model.StatusAnuncio;

public class AnuncioCarroFilter {

    private String titulo;
    private String marca;
    private StatusAnuncio status;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public StatusAnuncio getStatus() { return status; }
    public void setStatus(StatusAnuncio status) { this.status = status; }
}