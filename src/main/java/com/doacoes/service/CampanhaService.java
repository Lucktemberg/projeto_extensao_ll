package com.doacoes.service;

import com.doacoes.dto.CampanhaItemProgressoDTO;
import com.doacoes.dto.CampanhaItemRequestDTO;
import com.doacoes.dto.CampanhaRequestDTO;
import com.doacoes.dto.CampanhaResponseDTO;
import com.doacoes.entity.Campanha;
import com.doacoes.entity.CampanhaItem;
import com.doacoes.entity.Doacao;
import com.doacoes.repository.CampanhaRepository;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.util.StatusDoacao;
import com.doacoes.util.Textos;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final DoacaoRepository doacaoRepository;

    public CampanhaService(
            CampanhaRepository campanhaRepository,
            DoacaoRepository doacaoRepository) {

        this.campanhaRepository = campanhaRepository;
        this.doacaoRepository = doacaoRepository;
    }

    @Transactional
    public CampanhaResponseDTO salvar(CampanhaRequestDTO dto) {
        Campanha campanha = new Campanha();
        preencher(campanha, dto);
        return toResponse(campanhaRepository.save(campanha));
    }

    @Transactional(readOnly = true)
    public List<CampanhaResponseDTO> listar(boolean somenteAtivas) {
        List<Campanha> campanhas = somenteAtivas
                ? campanhaRepository.findAtivasWithItens()
                : campanhaRepository.findAllWithItens();

        return campanhas.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CampanhaResponseDTO buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public CampanhaResponseDTO atualizar(Long id, CampanhaRequestDTO dto) {
        Campanha campanha = buscarEntidade(id);
        preencher(campanha, dto);
        return toResponse(campanhaRepository.save(campanha));
    }

    @Transactional
    public void deletar(Long id) {
        Campanha campanha = buscarEntidade(id);

        if (doacaoRepository.existsByCampanhaId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir esta campanha porque já existem doações vinculadas."
            );
        }

        campanhaRepository.delete(campanha);
    }

    public Campanha buscarEntidade(Long id) {
        return campanhaRepository.findByIdWithItens(id)
                .orElseThrow(() -> new EntityNotFoundException("Campanha não encontrada"));
    }

    private void preencher(Campanha campanha, CampanhaRequestDTO dto) {
        campanha.setNome(dto.getNome().trim());
        campanha.setDescricao(Textos.opcional(dto.getDescricao()));
        campanha.setMetaQuantidade(dto.getMetaQuantidade());
        campanha.setDataInicio(dto.getDataInicio());
        campanha.setDataFim(dto.getDataFim());
        campanha.setAtiva(dto.getAtiva() == null || dto.getAtiva());

        if (dto.getDataInicio() != null
                && dto.getDataFim() != null
                && dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new IllegalArgumentException(
                    "A data final da campanha não pode ser anterior à data inicial"
            );
        }

        List<CampanhaItem> itens = new ArrayList<>();
        if (dto.getItens() != null) {
            for (CampanhaItemRequestDTO itemDto : dto.getItens()) {
                CampanhaItem item = new CampanhaItem();
                item.setDescricaoItem(itemDto.getDescricaoItem().trim());
                item.setCategoria(itemDto.getCategoria().trim().toUpperCase(Locale.ROOT));
                item.setMetaQuantidade(itemDto.getMetaQuantidade());
                itens.add(item);
            }
        }

        int somaItens = itens.stream()
                .mapToInt(item -> item.getMetaQuantidade() == null ? 0 : item.getMetaQuantidade())
                .sum();

        if (somaItens > 0 && somaItens > dto.getMetaQuantidade()) {
            throw new IllegalArgumentException(
                    "A soma das metas por item não pode ser maior que a meta geral da campanha"
            );
        }

        campanha.substituirItens(itens);
    }

    public CampanhaResponseDTO toResponse(Campanha campanha) {
        List<Doacao> doacoes = doacaoRepository.findByCampanhaId(campanha.getId())
                .stream()
                .filter(doacao -> StatusDoacao.contabilizaCampanha(doacao.getStatus()))
                .toList();

        int arrecadado = doacoes.stream()
                .mapToInt(doacao -> doacao.getQuantidade() == null ? 0 : doacao.getQuantidade())
                .sum();

        int meta = campanha.getMetaQuantidade() == null ? 0 : campanha.getMetaQuantidade();
        int faltam = Math.max(0, meta - arrecadado);
        int percentual = meta <= 0 ? 0 : Math.min(100, Math.round((arrecadado * 100f) / meta));

        List<CampanhaItemProgressoDTO> itens = campanha.getItens()
                .stream()
                .map(item -> {
                    int itemArrecadado = doacoes.stream()
                            .filter(doacao -> mesmoItem(doacao, item))
                            .mapToInt(doacao -> doacao.getQuantidade() == null ? 0 : doacao.getQuantidade())
                            .sum();
                    int itemMeta = item.getMetaQuantidade() == null ? 0 : item.getMetaQuantidade();
                    return new CampanhaItemProgressoDTO(
                            item.getDescricaoItem(),
                            item.getCategoria(),
                            itemMeta,
                            itemArrecadado,
                            Math.max(0, itemMeta - itemArrecadado)
                    );
                })
                .toList();

        return new CampanhaResponseDTO(
                campanha,
                arrecadado,
                faltam,
                percentual,
                doacoes.size(),
                itens
        );
    }

    private boolean mesmoItem(Doacao doacao, CampanhaItem item) {
        return normalizarChave(doacao.getDescricaoItem())
                .equals(normalizarChave(item.getDescricaoItem()));
    }

    private String normalizarChave(String valor) {
        if (valor == null) {
            return "";
        }

        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .trim();
    }
}
