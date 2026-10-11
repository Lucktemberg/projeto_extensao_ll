package com.doacoes.dto;

import com.doacoes.entity.Campanha;

import java.time.LocalDate;
import java.util.List;

public class CampanhaResponseDTO {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final Integer metaQuantidade;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final Boolean ativa;
    private final int arrecadado;
    private final int faltam;
    private final int percentual;
    private final int totalDoacoes;
    private final List<CampanhaItemProgressoDTO> itens;

    public CampanhaResponseDTO(
            Campanha campanha,
            int arrecadado,
            int faltam,
            int percentual,
            int totalDoacoes,
            List<CampanhaItemProgressoDTO> itens) {

        this.id = campanha.getId();
        this.nome = campanha.getNome();
        this.descricao = campanha.getDescricao();
        this.metaQuantidade = campanha.getMetaQuantidade();
        this.dataInicio = campanha.getDataInicio();
        this.dataFim = campanha.getDataFim();
        this.ativa = campanha.getAtiva();
        this.arrecadado = arrecadado;
        this.faltam = faltam;
        this.percentual = percentual;
        this.totalDoacoes = totalDoacoes;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getMetaQuantidade() {
        return metaQuantidade;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public int getArrecadado() {
        return arrecadado;
    }

    public int getFaltam() {
        return faltam;
    }

    public int getPercentual() {
        return percentual;
    }

    public int getTotalDoacoes() {
        return totalDoacoes;
    }

    public List<CampanhaItemProgressoDTO> getItens() {
        return itens;
    }
}
