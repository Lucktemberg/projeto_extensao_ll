package com.doacoes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CampanhaItemRequestDTO {

    @NotBlank(message = "Informe o item da meta")
    private String descricaoItem;

    @NotBlank(message = "Informe a categoria do item")
    private String categoria;

    @NotNull(message = "Informe a quantidade da meta")
    @Min(value = 1, message = "A meta do item deve ser maior que zero")
    private Integer metaQuantidade;

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getMetaQuantidade() {
        return metaQuantidade;
    }

    public void setMetaQuantidade(Integer metaQuantidade) {
        this.metaQuantidade = metaQuantidade;
    }
}
