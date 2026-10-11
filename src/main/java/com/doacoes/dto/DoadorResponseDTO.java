package com.doacoes.dto;

import com.doacoes.entity.Doador;

public class DoadorResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String endereco;
    private String telefone;
    private String email;
    private String observacao;

    public DoadorResponseDTO(Doador doador) {
        this.id = doador.getId();
        this.nome = doador.getNome();
        this.cpf = doador.getCpf();
        this.endereco = doador.getEndereco();
        this.telefone = doador.getTelefone();
        this.email = doador.getEmail();
        this.observacao = doador.getObservacao();
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