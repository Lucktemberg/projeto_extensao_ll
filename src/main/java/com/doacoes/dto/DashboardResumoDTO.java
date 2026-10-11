package com.doacoes.dto;

import java.util.List;

public class DashboardResumoDTO {

    private final long totalDoadores;
    private final long itensSemEstoque;
    private final long itensDisponiveis;
    private final long totalItensCadastrados;
    private final long totalUnidadesEmEstoque;
    private final long itensEstoqueBaixo;
    private final List<String> itensZerados;
    private final long doacoesEmAndamento;
    private final List<CampanhaResponseDTO> campanhasAtivas;

    public DashboardResumoDTO(
            long totalDoadores,
            long itensSemEstoque,
            long itensDisponiveis,
            long totalItensCadastrados,
            long totalUnidadesEmEstoque,
            long itensEstoqueBaixo,
            List<String> itensZerados,
            long doacoesEmAndamento,
            List<CampanhaResponseDTO> campanhasAtivas) {

        this.totalDoadores = totalDoadores;
        this.itensSemEstoque = itensSemEstoque;
        this.itensDisponiveis = itensDisponiveis;
        this.totalItensCadastrados = totalItensCadastrados;
        this.totalUnidadesEmEstoque = totalUnidadesEmEstoque;
        this.itensEstoqueBaixo = itensEstoqueBaixo;
        this.itensZerados = itensZerados;
        this.doacoesEmAndamento = doacoesEmAndamento;
        this.campanhasAtivas = campanhasAtivas;
    }

    public long getTotalDoadores() {
        return totalDoadores;
    }

    public long getItensSemEstoque() {
        return itensSemEstoque;
    }

    public long getItensDisponiveis() {
        return itensDisponiveis;
    }

    public long getTotalItensCadastrados() {
        return totalItensCadastrados;
    }

    public long getTotalUnidadesEmEstoque() {
        return totalUnidadesEmEstoque;
    }

    public long getItensEstoqueBaixo() {
        return itensEstoqueBaixo;
    }

    public List<String> getItensZerados() {
        return itensZerados;
    }

    public long getDoacoesEmAndamento() {
        return doacoesEmAndamento;
    }

    public List<CampanhaResponseDTO> getCampanhasAtivas() {
        return campanhasAtivas;
    }
}
