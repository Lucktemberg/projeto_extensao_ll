package com.doacoes.dto;

import java.time.LocalDate;
import java.util.List;

public class AcompanhamentoDoacaoDTO {

    private final String codigo;
    private final String descricaoItem;
    private final Integer quantidade;
    private final String categoria;
    private final LocalDate dataDoacao;
    private final LocalDate dataColeta;
    private final String status;
    private final String statusRotulo;
    private final String campanhaNome;
    private final String destino;
    private final List<AcompanhamentoEtapaDTO> etapas;

    public AcompanhamentoDoacaoDTO(
            String codigo,
            String descricaoItem,
            Integer quantidade,
            String categoria,
            LocalDate dataDoacao,
            LocalDate dataColeta,
            String status,
            String statusRotulo,
            String campanhaNome,
            String destino,
            List<AcompanhamentoEtapaDTO> etapas) {

        this.codigo = codigo;
        this.descricaoItem = descricaoItem;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.dataDoacao = dataDoacao;
        this.dataColeta = dataColeta;
        this.status = status;
        this.statusRotulo = statusRotulo;
        this.campanhaNome = campanhaNome;
        this.destino = destino;
        this.etapas = etapas;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusRotulo() {
        return statusRotulo;
    }

    public String getCampanhaNome() {
        return campanhaNome;
    }

    public String getDestino() {
        return destino;
    }

    public List<AcompanhamentoEtapaDTO> getEtapas() {
        return etapas;
    }
}
