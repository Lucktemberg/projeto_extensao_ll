package com.doacoes.dto;

import java.util.List;

public class RelatorioMensalDTO {

    private final int ano;
    private final int mes;
    private final String competencia;
    private final long totalDoacoes;
    private final long itensDoados;
    private final long totalDistribuicoes;
    private final long itensDistribuidos;
    private final List<ItemQuantidadeDTO> porStatus;
    private final List<ItemQuantidadeDTO> porCategoria;

    public RelatorioMensalDTO(
            int ano,
            int mes,
            String competencia,
            long totalDoacoes,
            long itensDoados,
            long totalDistribuicoes,
            long itensDistribuidos,
            List<ItemQuantidadeDTO> porStatus,
            List<ItemQuantidadeDTO> porCategoria) {

        this.ano = ano;
        this.mes = mes;
        this.competencia = competencia;
        this.totalDoacoes = totalDoacoes;
        this.itensDoados = itensDoados;
        this.totalDistribuicoes = totalDistribuicoes;
        this.itensDistribuidos = itensDistribuidos;
        this.porStatus = porStatus;
        this.porCategoria = porCategoria;
    }

    public int getAno() {
        return ano;
    }

    public int getMes() {
        return mes;
    }

    public String getCompetencia() {
        return competencia;
    }

    public long getTotalDoacoes() {
        return totalDoacoes;
    }

    public long getItensDoados() {
        return itensDoados;
    }

    public long getTotalDistribuicoes() {
        return totalDistribuicoes;
    }

    public long getItensDistribuidos() {
        return itensDistribuidos;
    }

    public List<ItemQuantidadeDTO> getPorStatus() {
        return porStatus;
    }

    public List<ItemQuantidadeDTO> getPorCategoria() {
        return porCategoria;
    }
}
