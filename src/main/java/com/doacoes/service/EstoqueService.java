package com.doacoes.service;

import com.doacoes.dto.EstoqueRequestDTO;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.DistribuicaoRepository;
import com.doacoes.repository.EstoqueRepository;
import com.doacoes.util.Descricoes;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final DistribuicaoRepository distribuicaoRepository;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            DistribuicaoRepository distribuicaoRepository) {

        this.estoqueRepository = estoqueRepository;
        this.distribuicaoRepository = distribuicaoRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Estoque salvar(
            EstoqueRequestDTO dto) {

        String descricao = Descricoes.normalizar(dto.getDescricao());
        String categoria = Descricoes.normalizar(dto.getCategoria());

        if (buscarPorDescricaoECategoria(descricao, categoria).isPresent()) {

            throw new IllegalArgumentException(
                    "Já existe um item de estoque com essa descrição e categoria"
            );
        }

        Estoque estoque = new Estoque();

        preencherEstoque(
                estoque,
                dto
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @Transactional
    public List<Estoque> listar() {
        consolidarDuplicatas();
        return estoqueRepository.findAllByOrderByDescricaoAsc();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public Estoque buscarPorId(Long id) {

        return estoqueRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Item de estoque não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public Estoque atualizar(
            Long id,
            EstoqueRequestDTO dto) {

        Estoque estoque =
                buscarPorId(id);

        String descricao = Descricoes.normalizar(dto.getDescricao());
        String categoria = Descricoes.normalizar(dto.getCategoria());

        buscarPorDescricaoECategoria(descricao, categoria)
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Já existe um item de estoque com essa descrição e categoria"
                    );
                });

        preencherEstoque(
                estoque,
                dto
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        Estoque estoque =
                buscarPorId(id);

        if (distribuicaoRepository.existsByEstoqueId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este item porque já existem distribuições vinculadas."
            );
        }

        estoqueRepository.delete(
                estoque
        );
    }

    // =====================================================
    // ADICIONAR QUANTIDADE
    // =====================================================

    @Transactional
    public Estoque adicionarQuantidade(
            Long id,
            int quantidade) {

        validarQuantidadeMaiorQueZero(
                quantidade
        );

        Estoque estoque =
                buscarPorId(id);

        estoque.setQuantidade(
                estoque.getQuantidade()
                        + quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // REMOVER QUANTIDADE
    // =====================================================

    @Transactional
    public Estoque removerQuantidade(
            Long id,
            int quantidade) {

        validarQuantidadeMaiorQueZero(
                quantidade
        );

        Estoque estoque =
                buscarPorId(id);

        if (estoque.getQuantidade()
                < quantidade) {

            throw new IllegalArgumentException(
                    "Quantidade insuficiente! Disponível: "
                            + estoque.getQuantidade()
            );
        }

        estoque.setQuantidade(
                estoque.getQuantidade()
                        - quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // ADICIONAR OU CRIAR ATRAVÉS DE DOAÇÃO
    // =====================================================

    @Transactional
    public Estoque adicionarOuCriarPorDoacao(
            String descricao,
            int quantidade,
            String categoria) {

        if (descricao == null
                || descricao.isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição do item inválida"
            );
        }

        if (quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade da doação deve ser maior que zero"
            );
        }

        if (categoria == null
                || categoria.isBlank()) {

            throw new IllegalArgumentException(
                    "Categoria inválida"
            );
        }

        String descricaoFormatada = Descricoes.normalizar(descricao);
        String categoriaFormatada = Descricoes.normalizar(categoria);

        Estoque estoque = buscarPorDescricaoECategoria(
                descricaoFormatada,
                categoriaFormatada
        ).orElse(null);

        /*
         * Se o item já existe,
         * apenas aumenta a quantidade.
         */
        if (estoque != null) {

            estoque.setQuantidade(
                    estoque.getQuantidade()
                            + quantidade
            );

            return estoqueRepository.save(
                    estoque
            );
        }

        /*
         * Se não existe,
         * cria um novo registro.
         */

        Estoque novoEstoque =
                new Estoque();

        novoEstoque.setDescricao(
                descricaoFormatada
        );

        novoEstoque.setQuantidade(
                quantidade
        );

        novoEstoque.setCategoria(
                categoriaFormatada
        );

        return estoqueRepository.save(
                novoEstoque
        );
    }

    // =====================================================
    // REMOVER POR DOAÇÃO
    // =====================================================
    /*
     * Este método é usado pelo DoacaoService
     * quando uma doação é alterada ou excluída.
     */
    @Transactional
    public Estoque removerPorDoacao(
            String descricao,
            Integer quantidade,
            String categoria) {

        if (descricao == null
                || descricao.isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição inválida"
            );
        }

        if (categoria == null
                || categoria.isBlank()) {

            throw new IllegalArgumentException(
                    "Categoria inválida"
            );
        }

        if (quantidade == null
                || quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero"
            );
        }

        String descricaoFormatada = Descricoes.normalizar(descricao);
        String categoriaFormatada = Descricoes.normalizar(categoria);

        Estoque estoque = buscarPorDescricaoECategoria(
                descricaoFormatada,
                categoriaFormatada
        ).orElseThrow(() ->
                new EntityNotFoundException(
                        "Item não encontrado no estoque"
                )
        );

        if (estoque.getQuantidade()
                < quantidade) {

            throw new IllegalArgumentException(
                    "Quantidade insuficiente no estoque. "
                            + "Disponível: "
                            + estoque.getQuantidade()
            );
        }

        estoque.setQuantidade(
                estoque.getQuantidade()
                        - quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // AJUSTAR ESTOQUE AO ATUALIZAR UMA DOAÇÃO
    // =====================================================
    /*
     * Esse método mantém a compatibilidade
     * com o DoacaoService que montamos antes.
     */
    @Transactional
    public void ajustarPorAtualizacao(
            String descricaoAntiga,
            Integer quantidadeAntiga,
            String categoriaAntiga,
            String descricaoNova,
            Integer quantidadeNova,
            String categoriaNova) {

        String descricaoAntigaFormatada = Descricoes.normalizar(descricaoAntiga);
        String descricaoNovaFormatada = Descricoes.normalizar(descricaoNova);
        String categoriaAntigaFormatada = Descricoes.normalizar(categoriaAntiga);
        String categoriaNovaFormatada = Descricoes.normalizar(categoriaNova);

        boolean mesmoItem =
                descricaoAntigaFormatada
                        .equals(
                                descricaoNovaFormatada
                        )
                        &&
                        categoriaAntigaFormatada
                                .equals(
                                        categoriaNovaFormatada
                                );

        /*
         * Se continua sendo o mesmo item,
         * apenas calculamos a diferença.
         */
        if (mesmoItem) {

            int diferenca =
                    quantidadeNova
                            - quantidadeAntiga;

            /*
             * Exemplo:
             *
             * Antes: 10
             * Depois: 15
             *
             * Adiciona 5.
             */
            if (diferenca > 0) {

                adicionarOuCriarPorDoacao(
                        descricaoNovaFormatada,
                        diferenca,
                        categoriaNovaFormatada
                );

            }

            /*
             * Exemplo:
             *
             * Antes: 10
             * Depois: 6
             *
             * Remove 4.
             */
            else if (diferenca < 0) {

                removerPorDoacao(
                        descricaoAntigaFormatada,
                        Math.abs(diferenca),
                        categoriaAntigaFormatada
                );
            }

            return;
        }

        /*
         * Se mudou item/categoria,
         * tira do item antigo.
         */
        removerPorDoacao(
                descricaoAntigaFormatada,
                quantidadeAntiga,
                categoriaAntigaFormatada
        );

        /*
         * E adiciona ao novo item.
         */
        adicionarOuCriarPorDoacao(
                descricaoNovaFormatada,
                quantidadeNova,
                categoriaNovaFormatada
        );
    }

    // =====================================================
    // PREENCHER / FORMATAR
    // =====================================================

    private void preencherEstoque(
            Estoque estoque,
            EstoqueRequestDTO dto) {

        estoque.setDescricao(Descricoes.normalizar(dto.getDescricao()));
        estoque.setQuantidade(dto.getQuantidade());
        estoque.setCategoria(Descricoes.normalizar(dto.getCategoria()));
    }

    public Estoque exigirPorDescricaoECategoria(String descricao, String categoria) {
        return buscarPorDescricaoECategoria(descricao, categoria)
                .orElseThrow(() ->
                        new EntityNotFoundException("Este item não está no estoque para entrega")
                );
    }

    private Optional<Estoque> buscarPorDescricaoECategoria(
            String descricao,
            String categoria) {

        String descricaoNormalizada = Descricoes.normalizar(descricao);
        String categoriaNormalizada = Descricoes.normalizar(categoria);

        Optional<Estoque> exato = estoqueRepository
                .findFirstByDescricaoIgnoreCaseAndCategoriaIgnoreCase(
                        descricaoNormalizada,
                        categoriaNormalizada
                );

        if (exato.isPresent()) {
            return exato;
        }

        return estoqueRepository.findAll().stream()
                .filter(item -> Descricoes.chave(item.getDescricao(), item.getCategoria())
                        .equals(Descricoes.chave(descricaoNormalizada, categoriaNormalizada)))
                .findFirst();
    }

    private void consolidarDuplicatas() {
        Map<String, Estoque> canonico = new LinkedHashMap<>();

        for (Estoque item : estoqueRepository.findAll()) {
            String descricao = Descricoes.normalizar(item.getDescricao());
            String categoria = Descricoes.normalizar(item.getCategoria());
            item.setDescricao(descricao);
            item.setCategoria(categoria);

            String chave = Descricoes.chave(descricao, categoria);
            Estoque existente = canonico.get(chave);

            if (existente == null) {
                canonico.put(chave, item);
                estoqueRepository.save(item);
                continue;
            }

            if (distribuicaoRepository.existsByEstoqueId(item.getId())) {
                estoqueRepository.save(item);
                continue;
            }

            existente.setQuantidade(
                    existente.getQuantidade() + item.getQuantidade()
            );
            estoqueRepository.save(existente);
            estoqueRepository.delete(item);
        }
    }

    // =====================================================
    // VALIDAR QUANTIDADE
    // =====================================================

    private void validarQuantidadeMaiorQueZero(
            int quantidade) {

        if (quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero"
            );
        }
    }
}