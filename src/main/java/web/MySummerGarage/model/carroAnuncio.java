package web.MySummerGarage.model;


public class carroAnuncio{
    private String marca;
    private String modelo;
    private int ano;
    private int quilometragem;
    private String descricao;
    private String dataAnuncio;

    public carroAnuncio(String marca, String modelo, int ano, int quilometragem) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quilometragem = quilometragem;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public int getQuilometragem() {
        return quilometragem;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataAnuncio() {
        return dataAnuncio;
    }
    

}