package com.doacoes.dto;

import com.doacoes.entity.Receptor;

public class ReceptorResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
    private String email;
    private String observacao;

    public ReceptorResponseDTO(Receptor receptor) {
        this.id = receptor.getId();
        this.nome = receptor.getNome();
        this.cpf = receptor.getCpf();
        this.endereco = receptor.getEndereco();
        this.telefone = receptor.getTelefone();
        this.email = receptor.getEmail();
        this.observacao = receptor.getObservacao();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getObservacao() {
        return observacao;
    }
}