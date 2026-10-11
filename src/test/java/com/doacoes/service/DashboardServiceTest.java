package com.doacoes.service;

import com.doacoes.dto.DashboardResumoDTO;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.repository.DoadorRepository;
import com.doacoes.repository.EstoqueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    private final DoadorRepository doadorRepository =
            mock(DoadorRepository.class);
    private final EstoqueRepository estoqueRepository =
            mock(EstoqueRepository.class);
    private final DoacaoRepository doacaoRepository =
            mock(DoacaoRepository.class);
    private final CampanhaService campanhaService =
            mock(CampanhaService.class);
    private final DashboardService dashboardService =
            new DashboardService(
                    doadorRepository,
                    estoqueRepository,
                    doacaoRepository,
                    campanhaService
            );

    @Test
    void deveMontarResumoComDadosDosRepositorios() {
        Estoque arroz = new Estoque();
        arroz.setDescricao("ARROZ");

        when(doadorRepository.count()).thenReturn(12L);
        when(estoqueRepository.count()).thenReturn(8L);
        when(estoqueRepository.countItensSemEstoque()).thenReturn(3L);
        when(estoqueRepository.somarQuantidadeTotal()).thenReturn(47L);
        when(estoqueRepository.countByQuantidadeGreaterThanAndQuantidadeLessThanEqual(0, 3))
                .thenReturn(2L);
        when(estoqueRepository.findByQuantidadeLessThanEqualOrderByDescricaoAsc(0))
                .thenReturn(List.of(arroz));
        when(doacaoRepository.countByStatusIn(List.of("CADASTRADA", "COLETA_AGENDADA")))
                .thenReturn(4L);
        when(campanhaService.listar(true)).thenReturn(List.of());

        DashboardResumoDTO resumo = dashboardService.obterResumo();

        assertEquals(12L, resumo.getTotalDoadores());
        assertEquals(3L, resumo.getItensSemEstoque());
        assertEquals(5L, resumo.getItensDisponiveis());
        assertEquals(8L, resumo.getTotalItensCadastrados());
        assertEquals(47L, resumo.getTotalUnidadesEmEstoque());
        assertEquals(2L, resumo.getItensEstoqueBaixo());
        assertEquals(List.of("ARROZ"), resumo.getItensZerados());
        assertEquals(4L, resumo.getDoacoesEmAndamento());
        assertEquals(List.of(), resumo.getCampanhasAtivas());
    }

    @Test
    void deveTratarSomaNulaComoEstoqueVazio() {
        when(estoqueRepository.somarQuantidadeTotal()).thenReturn(null);
        when(estoqueRepository.findByQuantidadeLessThanEqualOrderByDescricaoAsc(0))
                .thenReturn(List.of());
        when(campanhaService.listar(true)).thenReturn(List.of());

        DashboardResumoDTO resumo = dashboardService.obterResumo();

        assertEquals(0L, resumo.getTotalUnidadesEmEstoque());
        assertEquals(0L, resumo.getItensDisponiveis());
        assertEquals(List.of(), resumo.getItensZerados());
    }
}
