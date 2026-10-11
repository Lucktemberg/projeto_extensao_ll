package com.doacoes.dto;

import com.doacoes.entity.Receptor;

public class DestinoEntregaDTO {

    private final Long id;
    private final String nome;
    private final String endereco;
    private final String telefone;

    public DestinoEntregaDTO(Receptor receptor) {
        this.id = receptor.getId();
        this.nome = receptor.getNome();
        this.endereco = receptor.getEndereco();
        this.telefone = receptor.getTelefone();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }
}
