package com.doacoes.service;

import com.doacoes.dto.CampanhaItemRequestDTO;
import com.doacoes.dto.CampanhaRequestDTO;
import com.doacoes.dto.CampanhaResponseDTO;
import com.doacoes.entity.Campanha;
import com.doacoes.entity.CampanhaItem;
import com.doacoes.entity.Doacao;
import com.doacoes.repository.CampanhaRepository;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.util.StatusDoacao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CampanhaServiceTest {

    private final CampanhaRepository campanhaRepository = mock(CampanhaRepository.class);
    private final DoacaoRepository doacaoRepository = mock(DoacaoRepository.class);
    private final CampanhaService campanhaService =
            new CampanhaService(campanhaRepository, doacaoRepository);

    @Test
    void deveCalcularProgressoSemExporReceptor() {
        Campanha campanha = new Campanha();
        campanha.setId(1L);
        campanha.setNome("Inverno Solidário");
        campanha.setMetaQuantidade(200);
        campanha.setAtiva(true);

        CampanhaItem casaco = new CampanhaItem();
        casaco.setDescricaoItem("Casaco");
        casaco.setCategoria("VESTUARIO");
        casaco.setMetaQuantidade(80);
        campanha.getItens().add(casaco);

        Doacao recebida = new Doacao();
        recebida.setDescricaoItem("Casaco");
        recebida.setQuantidade(30);
        recebida.setStatus(StatusDoacao.RECEBIDA);

        Doacao cadastrada = new Doacao();
        cadastrada.setDescricaoItem("Casaco");
        cadastrada.setQuantidade(10);
        cadastrada.setStatus(StatusDoacao.CADASTRADA);

        when(doacaoRepository.findByCampanhaId(1L))
                .thenReturn(List.of(recebida, cadastrada));

        CampanhaResponseDTO progresso = campanhaService.toResponse(campanha);

        assertEquals(30, progresso.getArrecadado());
        assertEquals(170, progresso.getFaltam());
        assertEquals(15, progresso.getPercentual());
        assertEquals(1, progresso.getTotalDoacoes());
        assertEquals(50, progresso.getItens().get(0).getFaltam());
    }

    @Test
    void deveRejeitarMetaDeItensMaiorQueAMetaGeral() {
        CampanhaRequestDTO dto = new CampanhaRequestDTO();
        dto.setNome("Inverno Solidário");
        dto.setMetaQuantidade(50);

        CampanhaItemRequestDTO item = new CampanhaItemRequestDTO();
        item.setDescricaoItem("Casaco");
        item.setCategoria("VESTUARIO");
        item.setMetaQuantidade(80);
        dto.setItens(List.of(item));

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> campanhaService.salvar(dto)
        );

        assertEquals(
                "A soma das metas por item não pode ser maior que a meta geral da campanha",
                erro.getMessage()
        );
    }
}
