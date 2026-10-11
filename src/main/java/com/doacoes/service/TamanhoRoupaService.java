package com.doacoes.service;

import com.doacoes.dto.TamanhoRoupaRequestDTO;
import com.doacoes.entity.TamanhoRoupa;
import com.doacoes.repository.TamanhoRoupaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TamanhoRoupaService {

    private final TamanhoRoupaRepository tamanhoRoupaRepository;

    public TamanhoRoupaService(
            TamanhoRoupaRepository tamanhoRoupaRepository) {

        this.tamanhoRoupaRepository =
                tamanhoRoupaRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public TamanhoRoupa salvar(
            TamanhoRoupaRequestDTO dto) {

        String tamanho =
                formatar(dto.getTamanho());

        String tipo =
                formatar(dto.getTipo());

        verificarDuplicidade(
                tamanho,
                tipo,
                null
        );

        TamanhoRoupa tamanhoRoupa =
                new TamanhoRoupa();

        tamanhoRoupa.setTamanho(
                tamanho
        );

        tamanhoRoupa.setTipo(
                tipo
        );

        return tamanhoRoupaRepository.save(
                tamanhoRoupa
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<TamanhoRoupa> listar() {

        return tamanhoRoupaRepository
                .findAllByOrderByTamanhoAsc();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public TamanhoRoupa buscarPorId(
            Long id) {

        return tamanhoRoupaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Tamanho de roupa não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public TamanhoRoupa atualizar(
            Long id,
            TamanhoRoupaRequestDTO dto) {

        TamanhoRoupa tamanhoRoupa =
                buscarPorId(id);

        String tamanho =
                formatar(dto.getTamanho());

        String tipo =
                formatar(dto.getTipo());

        /*
         * Verifica se existe outro registro
         * com o mesmo tamanho e tipo.
         */
        verificarDuplicidade(
                tamanho,
                tipo,
                id
        );

        tamanhoRoupa.setTamanho(
                tamanho
        );

        tamanhoRoupa.setTipo(
                tipo
        );

        return tamanhoRoupaRepository.save(
                tamanhoRoupa
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        TamanhoRoupa tamanhoRoupa =
                buscarPorId(id);

        tamanhoRoupaRepository.delete(
                tamanhoRoupa
        );
    }

    // =====================================================
    // VERIFICAR DUPLICIDADE
    // =====================================================

    private void verificarDuplicidade(
            String tamanho,
            String tipo,
            Long idAtual) {

        Optional<TamanhoRoupa> existente =
                tamanhoRoupaRepository
                        .findFirstByTamanhoIgnoreCaseAndTipoIgnoreCase(
                                tamanho,
                                tipo
                        );

        /*
         * Nenhum registro encontrado.
         */
        if (existente.isEmpty()) {
            return;
        }

        /*
         * CREATE
         *
         * Se idAtual for null e encontrou
         * um registro, então já existe.
         */
        if (idAtual == null) {

            throw new IllegalArgumentException(
                    "Esse tamanho já existe para esse tipo"
            );
        }

        /*
         * UPDATE
         *
         * Permite atualizar o próprio registro,
         * mas não permite usar tamanho/tipo
         * pertencente a outro registro.
         */
        if (!existente
                .get()
                .getId()
                .equals(idAtual)) {

            throw new IllegalArgumentException(
                    "Esse tamanho já existe para esse tipo"
            );
        }
    }

    // =====================================================
    // FORMATAR
    // =====================================================

    private String formatar(
            String valor) {

        return valor
                .trim()
                .toUpperCase();
    }
}