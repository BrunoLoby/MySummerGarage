package web.MySummerGarage.controller;

import web.MySummerGarage.model.carroAnuncio;

public class carroAnuncioController {

    public void criarAnuncio(String marca, String modelo, int ano, int quilometragem){
        carroAnuncio anuncio = new carroAnuncio(marca, modelo, ano, quilometragem);
        System.out.println("Anúncio criado: " + marca + " " + modelo + ", Ano: " + ano + ", Quilometragem: " + quilometragem);
    }


    public void editarAnuncio(carroAnuncio anuncio, String novaDescricao){
        anuncio.setDescricao(novaDescricao);
        System.out.println("Anúncio editado. Nova descrição: " + novaDescricao);
    }

    public void removerAnuncio(carroAnuncio anuncio){
        System.out.println("Anúncio removido: " + anuncio.getMarca() + " " + anuncio.getModelo());
    }

    public void listarAnuncios(carroAnuncio[] anuncios){
        System.out.println("Lista de Anúncios:");
        for(carroAnuncio anuncio : anuncios){
            System.out.println(anuncio.getMarca() + " " + anuncio.getModelo() + ", Ano: " + anuncio.getAno() + ", Quilometragem: " + anuncio.getQuilometragem());
        }
    }

}
