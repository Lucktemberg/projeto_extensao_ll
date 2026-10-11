package com.doacoes.dto;

public class ItemQuantidadeDTO {

    private final String nome;
    private final long quantidade;

    public ItemQuantidadeDTO(String nome, long quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public long getQuantidade() {
        return quantidade;
    }
}
