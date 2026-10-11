package com.doacoes.service;

import com.doacoes.dto.AcompanhamentoDoacaoDTO;
import com.doacoes.dto.AcompanhamentoEtapaDTO;
import com.doacoes.dto.DestinoEntregaDTO;
import com.doacoes.dto.DistribuicaoRequestDTO;
import com.doacoes.dto.DistribuirDoacaoRequestDTO;
import com.doacoes.dto.DoacaoRequestDTO;
import com.doacoes.dto.DoacaoResponseDTO;
import com.doacoes.dto.DoacaoStatusRequestDTO;
import com.doacoes.entity.Campanha;
import com.doacoes.entity.Doador;
import com.doacoes.entity.Doacao;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.CampanhaRepository;
import com.doacoes.repository.DoadorRepository;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.util.Descricoes;
import com.doacoes.util.StatusDoacao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
public class DoacaoService {

    private static final String ALFABETO_CODIGO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private final DoacaoRepository doacaoRepository;
    private final DoadorRepository doadorRepository;
    private final EstoqueService estoqueService;
    private final CampanhaRepository campanhaRepository;
    private final DistribuicaoService distribuicaoService;
    private final ReceptorService receptorService;

    public DoacaoService(
            DoacaoRepository doacaoRepository,
            DoadorRepository doadorRepository,
            EstoqueService estoqueService,
            CampanhaRepository campanhaRepository,
            @Lazy DistribuicaoService distribuicaoService,
            ReceptorService receptorService) {

        this.doacaoRepository = doacaoRepository;
        this.doadorRepository = doadorRepository;
        this.estoqueService = estoqueService;
        this.campanhaRepository = campanhaRepository;
        this.distribuicaoService = distribuicaoService;
        this.receptorService = receptorService;
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public Doacao salvar(DoacaoRequestDTO dto) {

        validar(dto);

        Doacao doacao = new Doacao();

        preencherDoacao(doacao, dto);
        doacao.setStatus(StatusDoacao.CADASTRADA);
        doacao.setCodigoAcompanhamento(gerarCodigoUnico());

        return doacaoRepository.save(doacao);
    }

    // =========================
    // READ
    // =========================

    @Transactional
    public List<Doacao> listar() {
        return doacaoRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::completarCadastroLegado)
                .toList();
    }

    public List<DoacaoResponseDTO> listarRespostas() {
        return listar().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public Doacao buscarPorId(Long id) {
        return completarCadastroLegado(
                doacaoRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Doação não encontrada"
                                ))
        );
    }

    public DoacaoResponseDTO buscarResposta(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional
    public AcompanhamentoDoacaoDTO acompanhar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Informe o código de acompanhamento");
        }

        Doacao doacao = completarCadastroLegado(
                doacaoRepository.findByCodigoAcompanhamentoIgnoreCase(codigo.trim())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Não encontramos uma doação com este código"
                                ))
        );

        return montarAcompanhamento(doacao);
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public Doacao atualizar(
            Long id,
            DoacaoRequestDTO dto) {

        validar(dto);

        Doacao doacao = buscarPorId(id);

        // Guarda os dados antigos antes da atualização
        String descricaoAntiga =
                montarDescricaoParaEstoque(doacao);

        Integer quantidadeAntiga =
                doacao.getQuantidade();

        String categoriaAntiga =
                doacao.getCategoria();

        preencherDoacao(doacao, dto);

        if (StatusDoacao.contabilizaEstoque(doacao.getStatus())) {
            estoqueService.ajustarPorAtualizacao(
                    descricaoAntiga,
                    quantidadeAntiga,
                    categoriaAntiga,
                    montarDescricaoParaEstoque(doacao),
                    doacao.getQuantidade(),
                    doacao.getCategoria()
            );
        }

        return doacaoRepository.save(doacao);
    }

    @Transactional
    public Doacao atualizarStatus(Long id, DoacaoStatusRequestDTO dto) {
        Doacao doacao = buscarPorId(id);
        String statusAtual = StatusDoacao.normalizar(doacao.getStatus());
        String statusNovo = StatusDoacao.normalizar(dto.getStatus());

        if (StatusDoacao.DISTRIBUIDA.equals(statusNovo)
                && !StatusDoacao.DISTRIBUIDA.equals(statusAtual)) {
            throw new IllegalArgumentException(
                    "Para distribuir, escolha o receptor e o endereço de entrega."
            );
        }

        if (!StatusDoacao.transicaoPermitida(statusAtual, statusNovo)) {
            throw new IllegalArgumentException(
                    "Não é possível alterar a etapa de "
                            + StatusDoacao.rotulo(statusAtual)
                            + " para "
                            + StatusDoacao.rotulo(statusNovo)
            );
        }

        if (StatusDoacao.COLETA_AGENDADA.equals(statusNovo)) {
            doacao.setDataColeta(
                    dto.getDataColeta() == null ? LocalDate.now() : dto.getDataColeta()
            );
        }

        if (StatusDoacao.CADASTRADA.equals(statusNovo)) {
            doacao.setDataColeta(null);
        }

        boolean estavaNoEstoque = StatusDoacao.contabilizaEstoque(statusAtual);
        boolean ficaraNoEstoque = StatusDoacao.contabilizaEstoque(statusNovo);

        if (!estavaNoEstoque && ficaraNoEstoque) {
            estoqueService.adicionarOuCriarPorDoacao(
                    montarDescricaoParaEstoque(doacao),
                    doacao.getQuantidade(),
                    doacao.getCategoria()
            );
        }

        if (estavaNoEstoque && !ficaraNoEstoque) {
            try {
                estoqueService.removerPorDoacao(
                        montarDescricaoParaEstoque(doacao),
                        doacao.getQuantidade(),
                        doacao.getCategoria()
                );
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Não é possível voltar esta doação porque parte dos itens já saiu do estoque."
                );
            }
        }

        doacao.setStatus(statusNovo);
        return doacaoRepository.save(doacao);
    }

    @Transactional
    public Doacao distribuir(Long id, DistribuirDoacaoRequestDTO dto) {
        Doacao doacao = buscarPorId(id);
        String status = StatusDoacao.normalizar(doacao.getStatus());

        if (!StatusDoacao.RECEBIDA.equals(status)) {
            throw new IllegalArgumentException(
                    "Só é possível entregar doações que já foram recebidas"
            );
        }

        Estoque estoque = estoqueService.exigirPorDescricaoECategoria(
                montarDescricaoParaEstoque(doacao),
                doacao.getCategoria()
        );

        int disponivel = doacao.getQuantidade() == null ? 0 : doacao.getQuantidade();
        int quantidade = dto.getQuantidade() == null ? disponivel : dto.getQuantidade();

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Informe uma quantidade válida para a entrega");
        }

        if (quantidade > disponivel) {
            throw new IllegalArgumentException(
                    "A quantidade da entrega não pode ser maior que " + disponivel
            );
        }

        DistribuicaoRequestDTO distribuicao = new DistribuicaoRequestDTO();
        distribuicao.setEstoqueId(estoque.getId());
        distribuicao.setReceptorId(dto.getReceptorId());
        distribuicao.setQuantidade(quantidade);
        distribuicao.setDataDistribuicao(dto.getDataDistribuicao());
        distribuicao.setObservacao(dto.getObservacao());

        distribuicaoService.salvar(distribuicao, false);

        if (quantidade == disponivel) {
            doacao.setStatus(StatusDoacao.DISTRIBUIDA);
        } else {
            doacao.setQuantidade(disponivel - quantidade);
        }

        return doacaoRepository.save(doacao);
    }

    public List<DestinoEntregaDTO> listarDestinos() {
        return receptorService.listar().stream()
                .map(DestinoEntregaDTO::new)
                .toList();
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deletar(Long id) {

        Doacao doacao = buscarPorId(id);

        String descricao =
                montarDescricaoParaEstoque(doacao);

        if (StatusDoacao.contabilizaEstoque(doacao.getStatus())) {
            try {
                estoqueService.removerPorDoacao(
                        descricao,
                        doacao.getQuantidade(),
                        doacao.getCategoria()
                );
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Não é possível excluir esta doação porque parte dos itens já foi distribuída ou o estoque foi alterado."
                );
            }
        }

        doacaoRepository.delete(doacao);
    }

    @Transactional
    public int marcarDistribuidas(String descricaoEstoque, String categoria, int quantidade) {
        if (quantidade <= 0) {
            return 0;
        }

        String alvo = Descricoes.normalizar(descricaoEstoque);
        String categoriaAlvo = Descricoes.normalizar(categoria);
        int restante = quantidade;
        int marcadas = 0;

        List<Doacao> candidatas = doacaoRepository
                .findByStatusAndCategoriaIgnoreCaseOrderByIdAsc(
                        StatusDoacao.RECEBIDA,
                        categoriaAlvo
                );

        for (Doacao doacao : candidatas) {
            if (restante <= 0) {
                break;
            }

            String descricao = Descricoes.normalizar(montarDescricaoParaEstoque(doacao));
            if (!descricao.equals(alvo)) {
                continue;
            }

            int quantidadeDoacao = doacao.getQuantidade() == null ? 0 : doacao.getQuantidade();
            if (quantidadeDoacao <= 0 || quantidadeDoacao > restante) {
                continue;
            }

            doacao.setStatus(StatusDoacao.DISTRIBUIDA);
            doacaoRepository.save(doacao);
            restante -= quantidadeDoacao;
            marcadas++;
        }

        return marcadas;
    }

    // =========================
    // PESQUISAR
    // =========================

    @Transactional
    public List<Doacao> pesquisar(String termo) {

        if (termo == null || termo.isBlank()) {

            return listar();
        }

        return doacaoRepository.pesquisar(termo.trim())
                .stream()
                .map(this::completarCadastroLegado)
                .toList();
    }

    public List<DoacaoResponseDTO> pesquisarRespostas(String termo) {
        return pesquisar(termo).stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // PREENCHER ENTIDADE
    // =========================

    private void preencherDoacao(
            Doacao doacao,
            DoacaoRequestDTO dto) {

        preencherDoador(doacao, dto);

        doacao.setDescricaoItem(
                dto.getDescricaoItem().trim()
        );

        doacao.setQuantidade(
                dto.getQuantidade()
        );

        doacao.setCategoria(
                dto.getCategoria()
                        .trim()
                        .toUpperCase()
        );

        String unidade = dto.getUnidade();
        if (unidade == null || unidade.isBlank()) {
            unidade = "VESTUARIO".equals(doacao.getCategoria()) ? "UN" : unidade;
        }

        if (unidade == null || unidade.isBlank()) {
            throw new IllegalArgumentException(
                    "Unidade é obrigatória para alimentos"
            );
        }

        doacao.setUnidade(unidade.trim().toUpperCase());
        doacao.setDetalheUnidade(normalizarTextoOpcional(dto.getDetalheUnidade()));

        if (dto.getTamanho() != null && !dto.getTamanho().isBlank()) {
            doacao.setTamanho(dto.getTamanho().trim().toUpperCase());
        } else {
            doacao.setTamanho(null);
        }

        doacao.setDataDoacao(
                dto.getDataDoacao()
        );

        doacao.setObservacao(
                normalizarTextoOpcional(dto.getObservacao())
        );

        doacao.setCampanhaId(resolverCampanha(dto.getCampanhaId()));
    }

    private void preencherDoador(
            Doacao doacao,
            DoacaoRequestDTO dto) {

        if (dto.getDoadorId() == null) {
            doacao.setDoadorId(null);
            doacao.setNomeDoador(dto.getNomeDoador().trim().toUpperCase());
            return;
        }

        Doador doador = doadorRepository
                .findById(dto.getDoadorId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Doador não encontrado"
                        )
                );

        doacao.setDoadorId(doador.getId());
        doacao.setNomeDoador(doador.getNome());
    }

    private String normalizarTextoOpcional(String valor) {
        return valor == null || valor.isBlank()
                ? null
                : valor.trim();
    }

    // =========================
    // DESCRIÇÃO DO ESTOQUE
    // =========================

    private String montarDescricaoParaEstoque(
            Doacao doacao) {

        String descricao;

        if ("VESTUARIO".equals(doacao.getCategoria())
                && doacao.getTamanho() != null
                && !doacao.getTamanho().isBlank()) {

            descricao = doacao.getDescricaoItem()
                    + " - TAM "
                    + doacao.getTamanho();
        } else if ("ALIMENTOS".equals(doacao.getCategoria())) {
            descricao = doacao.getDescricaoItem();

            if (doacao.getUnidade() != null
                    && !doacao.getUnidade().isBlank()
                    && !unidadeNeutra(doacao.getUnidade())) {
                descricao += " - " + doacao.getUnidade();
            }

            if (doacao.getDetalheUnidade() != null
                    && !doacao.getDetalheUnidade().isBlank()) {
                descricao += " " + doacao.getDetalheUnidade();
            }
        } else {
            descricao = doacao.getDescricaoItem();
        }

        return Descricoes.normalizar(descricao);
    }

    private boolean unidadeNeutra(String unidade) {
        String unidadeFormatada = unidade.trim().toUpperCase();
        return "UN".equals(unidadeFormatada)
                || "UNIDADE".equals(unidadeFormatada);
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    private void validar(
            DoacaoRequestDTO dto) {

        if (dto.getDoadorId() == null
                && (dto.getNomeDoador() == null || dto.getNomeDoador().isBlank())) {
            throw new IllegalArgumentException(
                    "Informe o doador da doação"
            );
        }

        String categoria = dto.getCategoria().trim().toUpperCase();

        if (!"ALIMENTOS".equals(categoria)
                && !"VESTUARIO".equals(categoria)) {

            throw new IllegalArgumentException(
                    "Categoria inválida. Use ALIMENTOS ou VESTUARIO"
            );
        }

        if ("VESTUARIO".equals(categoria)
                && (dto.getTamanho() == null || dto.getTamanho().isBlank())) {
            throw new IllegalArgumentException(
                    "Tamanho é obrigatório para vestuário"
            );
        }

        if ("ALIMENTOS".equals(categoria)
                && (dto.getUnidade() == null || dto.getUnidade().isBlank())) {
            throw new IllegalArgumentException(
                    "Unidade é obrigatória para alimentos"
            );
        }
    }

    private Long resolverCampanha(Long campanhaId) {
        if (campanhaId == null) {
            return null;
        }

        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(() -> new EntityNotFoundException("Campanha não encontrada"));

        if (Boolean.FALSE.equals(campanha.getAtiva())) {
            throw new IllegalArgumentException("Esta campanha já foi encerrada");
        }

        return campanha.getId();
    }

    private Doacao completarCadastroLegado(Doacao doacao) {
        boolean atualizou = false;

        if (doacao.getStatus() == null || doacao.getStatus().isBlank()) {
            doacao.setStatus(StatusDoacao.RECEBIDA);
            atualizou = true;
        }

        if (doacao.getCodigoAcompanhamento() == null
                || doacao.getCodigoAcompanhamento().isBlank()) {
            doacao.setCodigoAcompanhamento(gerarCodigoUnico());
            atualizou = true;
        }

        return atualizou ? doacaoRepository.save(doacao) : doacao;
    }

    private String gerarCodigoUnico() {
        String codigo;
        do {
            StringBuilder gerado = new StringBuilder("SOL-");
            for (int i = 0; i < 6; i++) {
                gerado.append(
                        ALFABETO_CODIGO.charAt(ALEATORIO.nextInt(ALFABETO_CODIGO.length()))
                );
            }
            codigo = gerado.toString();
        } while (doacaoRepository.existsByCodigoAcompanhamento(codigo));

        return codigo;
    }

    public DoacaoResponseDTO toResponse(Doacao doacao) {
        DoacaoResponseDTO dto = new DoacaoResponseDTO(doacao);
        if (doacao.getCampanhaId() != null) {
            campanhaRepository.findById(doacao.getCampanhaId())
                    .ifPresent(campanha -> dto.setCampanhaNome(campanha.getNome()));
        }
        preencherContatoDoador(dto, doacao);
        return dto;
    }

    private void preencherContatoDoador(DoacaoResponseDTO dto, Doacao doacao) {
        if (doacao.getDoadorId() == null) {
            return;
        }

        doadorRepository.findById(doacao.getDoadorId()).ifPresent(doador -> {
            dto.setEmailDoador(doador.getEmail());
            dto.setTelefoneDoador(doador.getTelefone());

            String codigo = doacao.getCodigoAcompanhamento() == null
                    ? ""
                    : doacao.getCodigoAcompanhamento();
            String status = StatusDoacao.rotulo(doacao.getStatus());
            String mensagem = "Olá! Sua doação no Solidário (" + codigo
                    + ") está: " + status
                    + ". Acompanhe em /acompanhar com este código.";

            String telefone = apenasDigitos(doador.getTelefone());
            if (telefone.length() >= 10) {
                if (telefone.length() <= 11) {
                    telefone = "55" + telefone;
                }
                dto.setLinkWhatsapp(
                        "https://wa.me/" + telefone
                                + "?text=" + java.net.URLEncoder.encode(
                                mensagem,
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                );
            }

            if (doador.getEmail() != null && !doador.getEmail().isBlank()) {
                dto.setLinkEmail(
                        "mailto:" + doador.getEmail().trim()
                                + "?subject=" + java.net.URLEncoder.encode(
                                "Doação " + codigo,
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                                + "&body=" + java.net.URLEncoder.encode(
                                mensagem,
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                );
            }
        });
    }

    private String apenasDigitos(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    private AcompanhamentoDoacaoDTO montarAcompanhamento(Doacao doacao) {
        String status = StatusDoacao.normalizar(doacao.getStatus());
        int indiceAtual = StatusDoacao.ETAPAS.indexOf(status);

        List<AcompanhamentoEtapaDTO> etapas = StatusDoacao.ETAPAS.stream()
                .map(etapa -> new AcompanhamentoEtapaDTO(
                        etapa,
                        StatusDoacao.rotulo(etapa),
                        indiceAtual >= StatusDoacao.ETAPAS.indexOf(etapa),
                        etapa.equals(status)
                ))
                .toList();

        String campanhaNome = null;
        if (doacao.getCampanhaId() != null) {
            campanhaNome = campanhaRepository.findById(doacao.getCampanhaId())
                    .map(Campanha::getNome)
                    .orElse(null);
        }

        String destino = StatusDoacao.DISTRIBUIDA.equals(status)
                ? "A contribuição já foi encaminhada a quem precisa, sem identificação de quem recebeu."
                : StatusDoacao.RECEBIDA.equals(status)
                ? "Os itens já estão na instituição e seguem para distribuição."
                : "A doação ainda está em preparação. O destino será informado quando houver distribuição.";

        return new AcompanhamentoDoacaoDTO(
                doacao.getCodigoAcompanhamento(),
                doacao.getDescricaoItem(),
                doacao.getQuantidade(),
                doacao.getCategoria(),
                doacao.getDataDoacao(),
                doacao.getDataColeta(),
                status,
                StatusDoacao.rotulo(status),
                campanhaNome,
                destino,
                etapas
        );
    }
}