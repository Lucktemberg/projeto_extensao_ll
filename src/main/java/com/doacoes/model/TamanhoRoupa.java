package com.doacoes.model;

import com.doacoes.enums.TipoTamanho;

public class TamanhoRoupa {
    private long id;
    private String tamanho;
    private TipoTamanho categoria;

    public TamanhoRoupa() {

    }
    public TamanhoRoupa(long id, String tamanho, TipoTamanho tipoTamanho) {
        this.id = id;
        this.tamanho = tamanho;
        this.categoria = tipoTamanho;


    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;

    }
    public String getTamanho() {
        return tamanho;
    }
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }
    public TipoTamanho getCategoria() {
        return categoria;
    }
    public void setCategoria(TipoTamanho categoria) {
        this.categoria = categoria;

    }

    @Override
    public String toString() {
        return "TamanhoRoupa{" +
                "id=" + id +
                ", tamanho='" + tamanho + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}
