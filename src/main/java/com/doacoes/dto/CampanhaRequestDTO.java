package com.doacoes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampanhaRequestDTO {

    @NotBlank(message = "Informe o nome da campanha")
    private String nome;

    private String descricao;

    @NotNull(message = "Informe a meta da campanha")
    @Min(value = 1, message = "A meta deve ser maior que zero")
    private Integer metaQuantidade;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Boolean ativa = true;

    @Valid
    private List<CampanhaItemRequestDTO> itens = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMetaQuantidade() {
        return metaQuantidade;
    }

    public void setMetaQuantidade(Integer metaQuantidade) {
        this.metaQuantidade = metaQuantidade;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<CampanhaItemRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<CampanhaItemRequestDTO> itens) {
        this.itens = itens;
    }
}
