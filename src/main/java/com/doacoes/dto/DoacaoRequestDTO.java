package com.doacoes.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DoacaoRequestDTO {

    private String nomeDoador;

    @JsonAlias("idDoador")
    private Long doadorId;

    @NotBlank(message = "Descrição do item é obrigatória")
    private String descricaoItem;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    private String unidade;

    private String detalheUnidade;

    private String tamanho;

    @NotNull(message = "Data da doação é obrigatória")
    private LocalDate dataDoacao;

    private String observacao;

    private Long campanhaId;

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDetalheUnidade() {
        return detalheUnidade;
    }

    public void setDetalheUnidade(String detalheUnidade) {
        this.detalheUnidade = detalheUnidade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(LocalDate dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }
}