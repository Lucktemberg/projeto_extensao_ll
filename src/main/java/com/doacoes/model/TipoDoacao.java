package com.doacoes.model;

import com.doacoes.enums.CategoriaItem;

public class TipoDoacao {

    private Long id;
    private String nome;
    private CategoriaItem categoria;

    public TipoDoacao() {
    }

    public TipoDoacao(Long id, String nome, CategoriaItem categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaItem getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaItem categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "TipoDoacao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria=" + categoria +
                '}';
    }}