package com.doacoes.service;

import com.doacoes.dto.CampanhaResponseDTO;
import com.doacoes.dto.DashboardResumoDTO;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.repository.DoadorRepository;
import com.doacoes.repository.EstoqueRepository;
import com.doacoes.util.StatusDoacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final DoadorRepository doadorRepository;
    private final EstoqueRepository estoqueRepository;
    private final DoacaoRepository doacaoRepository;
    private final CampanhaService campanhaService;

    public DashboardService(
            DoadorRepository doadorRepository,
            EstoqueRepository estoqueRepository,
            DoacaoRepository doacaoRepository,
            CampanhaService campanhaService) {

        this.doadorRepository = doadorRepository;
        this.estoqueRepository = estoqueRepository;
        this.doacaoRepository = doacaoRepository;
        this.campanhaService = campanhaService;
    }

    @Transactional(readOnly = true)
    public DashboardResumoDTO obterResumo() {

        long totalDoadores = doadorRepository.count();
        long totalItensCadastrados = estoqueRepository.count();
        long itensSemEstoque = estoqueRepository.countItensSemEstoque();
        long itensDisponiveis =
                Math.max(0, totalItensCadastrados - itensSemEstoque);

        Long somaQuantidade = estoqueRepository.somarQuantidadeTotal();
        long totalUnidadesEmEstoque =
                somaQuantidade == null ? 0 : somaQuantidade;

        long itensEstoqueBaixo =
                estoqueRepository.countByQuantidadeGreaterThanAndQuantidadeLessThanEqual(
                        0,
                        3
                );

        List<String> itensZerados = estoqueRepository
                .findByQuantidadeLessThanEqualOrderByDescricaoAsc(0)
                .stream()
                .map(Estoque::getDescricao)
                .limit(8)
                .toList();

        long doacoesEmAndamento = doacaoRepository.countByStatusIn(
                List.of(StatusDoacao.CADASTRADA, StatusDoacao.COLETA_AGENDADA)
        );

        List<CampanhaResponseDTO> campanhasAtivas = campanhaService.listar(true)
                .stream()
                .limit(3)
                .toList();

        return new DashboardResumoDTO(
                totalDoadores,
                itensSemEstoque,
                itensDisponiveis,
                totalItensCadastrados,
                totalUnidadesEmEstoque,
                itensEstoqueBaixo,
                itensZerados,
                doacoesEmAndamento,
                campanhasAtivas
        );
    }
}
