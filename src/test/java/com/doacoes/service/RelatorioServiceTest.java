package com.doacoes.service;

import com.doacoes.dto.RelatorioMensalDTO;
import com.doacoes.entity.Distribuicao;
import com.doacoes.entity.Doacao;
import com.doacoes.repository.DistribuicaoRepository;
import com.doacoes.repository.DoacaoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RelatorioServiceTest {

    private final DoacaoRepository doacaoRepository = mock(DoacaoRepository.class);
    private final DistribuicaoRepository distribuicaoRepository = mock(DistribuicaoRepository.class);
    private final RelatorioService relatorioService =
            new RelatorioService(doacaoRepository, distribuicaoRepository);

    @Test
    void deveSomarDoacoesEDistribuicoesDoMes() {
        YearMonth periodo = YearMonth.now();

        Doacao doacao = new Doacao();
        doacao.setQuantidade(4);
        doacao.setStatus("RECEBIDA");
        doacao.setCategoria("VESTUARIO");

        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setQuantidade(2);

        when(doacaoRepository.findByDataDoacaoBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(doacao));
        when(distribuicaoRepository.findByDataBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(distribuicao));

        RelatorioMensalDTO relatorio = relatorioService.gerar(periodo.getYear(), periodo.getMonthValue());

        assertEquals(periodo.getYear(), relatorio.getAno());
        assertEquals(periodo.getMonthValue(), relatorio.getMes());
        assertEquals(1L, relatorio.getTotalDoacoes());
        assertEquals(4L, relatorio.getItensDoados());
        assertEquals(1L, relatorio.getTotalDistribuicoes());
        assertEquals(2L, relatorio.getItensDistribuidos());
        assertEquals(1, relatorio.getPorStatus().size());
        assertEquals(1, relatorio.getPorCategoria().size());
    }

    @Test
    void deveRecusarMesInvalido() {
        assertThrows(IllegalArgumentException.class, () -> relatorioService.gerar(2026, 13));
    }

    @Test
    void deveGerarCsvEPdfDoRelatorio() {
        when(doacaoRepository.findByDataDoacaoBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());
        when(distribuicaoRepository.findByDataBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        byte[] csv = relatorioService.exportarCsv(2026, 10);
        byte[] pdf = relatorioService.exportarPdf(2026, 10);

        org.junit.jupiter.api.Assertions.assertTrue(new String(csv).contains("Relatório mensal Solidário"));
        org.junit.jupiter.api.Assertions.assertTrue(pdf.length > 100);
        org.junit.jupiter.api.Assertions.assertEquals("%PDF", new String(pdf, 0, 4));
    }
}
