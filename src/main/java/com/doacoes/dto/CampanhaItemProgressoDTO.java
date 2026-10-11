package com.doacoes.dto;

public class CampanhaItemProgressoDTO {

    private final String descricaoItem;
    private final String categoria;
    private final int meta;
    private final int arrecadado;
    private final int faltam;

    public CampanhaItemProgressoDTO(
            String descricaoItem,
            String categoria,
            int meta,
            int arrecadado,
            int faltam) {

        this.descricaoItem = descricaoItem;
        this.categoria = categoria;
        this.meta = meta;
        this.arrecadado = arrecadado;
        this.faltam = faltam;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getMeta() {
        return meta;
    }

    public int getArrecadado() {
        return arrecadado;
    }

    public int getFaltam() {
        return faltam;
    }
}
