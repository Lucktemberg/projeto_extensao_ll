package com.doacoes.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DistribuicaoRequestDTO {

    @NotNull(message = "O estoque é obrigatório")
    private Long estoqueId;

    @NotNull(message = "O receptor é obrigatório")
    private Long receptorId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

    @JsonAlias("data")
    private LocalDate dataDistribuicao;

    private String observacao;

    public Long getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(Long estoqueId) {
        this.estoqueId = estoqueId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataDistribuicao() {
        return dataDistribuicao;
    }

    public void setDataDistribuicao(LocalDate dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}