package com.doacoes.dto;

import com.doacoes.entity.TipoDoacao;

public class TipoDoacaoResponseDTO {

    private Long id;
    private String nome;
    private String categoria;

    public TipoDoacaoResponseDTO(
            TipoDoacao tipoDoacao) {

        this.id = tipoDoacao.getId();
        this.nome = tipoDoacao.getNome();
        this.categoria = tipoDoacao.getCategoria();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }
}