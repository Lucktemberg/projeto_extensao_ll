package com.doacoes.service;

import com.doacoes.dto.DoacaoRequestDTO;
import com.doacoes.repository.CampanhaRepository;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.repository.DoadorRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DoacaoServiceTest {

    private final DoacaoService doacaoService = new DoacaoService(
            mock(DoacaoRepository.class),
            mock(DoadorRepository.class),
            mock(EstoqueService.class),
            mock(CampanhaRepository.class),
            mock(DistribuicaoService.class),
            mock(ReceptorService.class)
    );

    @Test
    void deveExigirDoadorNaDoacao() {
        DoacaoRequestDTO dto = new DoacaoRequestDTO();
        dto.setDescricaoItem("Arroz");
        dto.setQuantidade(2);
        dto.setCategoria("ALIMENTOS");
        dto.setUnidade("KG");
        dto.setDataDoacao(LocalDate.now());

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> doacaoService.salvar(dto)
        );

        assertEquals("Informe o doador da doação", erro.getMessage());
    }

    @Test
    void deveExigirUnidadeParaAlimentos() {
        DoacaoRequestDTO dto = new DoacaoRequestDTO();
        dto.setDoadorId(1L);
        dto.setDescricaoItem("Arroz");
        dto.setQuantidade(2);
        dto.setCategoria("ALIMENTOS");
        dto.setDataDoacao(LocalDate.now());

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> doacaoService.salvar(dto)
        );

        assertEquals("Unidade é obrigatória para alimentos", erro.getMessage());
    }

    @Test
    void deveTratarUnComoUnidadeNeutraNaDescricaoDeEstoque() {
        DoacaoService servico = doacaoService;
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            java.lang.reflect.Method metodo =
                    DoacaoService.class.getDeclaredMethod(
                            "unidadeNeutra",
                            String.class
                    );
            metodo.setAccessible(true);
            org.junit.jupiter.api.Assertions.assertTrue(
                    (boolean) metodo.invoke(servico, "UN")
            );
            org.junit.jupiter.api.Assertions.assertTrue(
                    (boolean) metodo.invoke(servico, "unidade")
            );
            org.junit.jupiter.api.Assertions.assertFalse(
                    (boolean) metodo.invoke(servico, "KG")
            );
        });
    }

    @Test
    void deveMarcarDoacoesRecebidasAoDistribuirQuantidade() {
        com.doacoes.repository.DoacaoRepository repositorio =
                mock(com.doacoes.repository.DoacaoRepository.class);
        com.doacoes.repository.DoadorRepository doadores =
                mock(com.doacoes.repository.DoadorRepository.class);
        com.doacoes.repository.CampanhaRepository campanhas =
                mock(com.doacoes.repository.CampanhaRepository.class);
        DoacaoService servico = new DoacaoService(
                repositorio,
                doadores,
                mock(EstoqueService.class),
                campanhas,
                mock(DistribuicaoService.class),
                mock(ReceptorService.class)
        );

        com.doacoes.entity.Doacao doacao = new com.doacoes.entity.Doacao();
        doacao.setId(1L);
        doacao.setStatus("RECEBIDA");
        doacao.setCategoria("VESTUARIO");
        doacao.setDescricaoItem("CAMISETA");
        doacao.setTamanho("G");
        doacao.setQuantidade(2);

        when(repositorio.findByStatusAndCategoriaIgnoreCaseOrderByIdAsc("RECEBIDA", "VESTUARIO"))
                .thenReturn(java.util.List.of(doacao));
        when(repositorio.save(doacao)).thenReturn(doacao);

        int marcadas = servico.marcarDistribuidas("CAMISETA - TAM G - ADULTO", "VESTUARIO", 2);

        assertEquals(1, marcadas);
        assertEquals("DISTRIBUIDA", doacao.getStatus());
    }

    @Test
    void deveExigirReceptorParaMarcarComoDistribuida() {
        com.doacoes.entity.Doacao doacao = new com.doacoes.entity.Doacao();
        doacao.setStatus("RECEBIDA");
        doacao.setCodigoAcompanhamento("SOL-TESTE1");

        com.doacoes.repository.DoacaoRepository repositorio =
                mock(com.doacoes.repository.DoacaoRepository.class);
        when(repositorio.findById(1L)).thenReturn(java.util.Optional.of(doacao));

        DoacaoService servico = new DoacaoService(
                repositorio,
                mock(DoadorRepository.class),
                mock(EstoqueService.class),
                mock(CampanhaRepository.class),
                mock(DistribuicaoService.class),
                mock(ReceptorService.class)
        );

        com.doacoes.dto.DoacaoStatusRequestDTO dto = new com.doacoes.dto.DoacaoStatusRequestDTO();
        dto.setStatus("DISTRIBUIDA");

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.atualizarStatus(1L, dto)
        );

        assertEquals(
                "Para distribuir, escolha o receptor e o endereço de entrega.",
                erro.getMessage()
        );
    }

    @Test
    void deveRecusarTransicaoInvalidaDeStatus() {
        org.junit.jupiter.api.Assertions.assertFalse(
                com.doacoes.util.StatusDoacao.transicaoPermitida(
                        "CADASTRADA",
                        "DISTRIBUIDA"
                )
        );
        org.junit.jupiter.api.Assertions.assertTrue(
                com.doacoes.util.StatusDoacao.transicaoPermitida(
                        "CADASTRADA",
                        "RECEBIDA"
                )
        );
    }
}
