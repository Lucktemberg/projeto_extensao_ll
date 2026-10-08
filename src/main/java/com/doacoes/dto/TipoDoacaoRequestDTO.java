package com.doacoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TipoDoacaoRequestDTO {

    @NotBlank(message = "Nome inválido")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ÿ\\s]+$",
            message = "O nome não pode conter números ou caracteres especiais"
    )
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    public TipoDoacaoRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}