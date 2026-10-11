package com.doacoes.service;

import com.doacoes.dto.ReceptorRequestDTO;
import com.doacoes.entity.Receptor;
import com.doacoes.repository.DistribuicaoRepository;
import com.doacoes.repository.ReceptorRepository;
import com.doacoes.util.Textos;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceptorService {

    private final ReceptorRepository receptorRepository;
    private final DistribuicaoRepository distribuicaoRepository;

    public ReceptorService(
            ReceptorRepository receptorRepository,
            DistribuicaoRepository distribuicaoRepository) {

        this.receptorRepository = receptorRepository;
        this.distribuicaoRepository = distribuicaoRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Receptor salvar(
            ReceptorRequestDTO dto) {

        validarCpf(dto.getCpf(), null);

        Receptor receptor =
                new Receptor();

        preencherReceptor(
                receptor,
                dto
        );

        return receptorRepository.save(
                receptor
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<Receptor> listar() {

        return receptorRepository.findAllByOrderByNomeAsc();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public Receptor buscarPorId(Long id) {

        return receptorRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Receptor não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public Receptor atualizar(
            Long id,
            ReceptorRequestDTO dto) {

        validarCpf(dto.getCpf(), id);

        Receptor receptor =
                buscarPorId(id);

        preencherReceptor(
                receptor,
                dto
        );

        return receptorRepository.save(
                receptor
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        Receptor receptor =
                buscarPorId(id);

        if (distribuicaoRepository.existsByReceptorId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este receptor porque já existem distribuições vinculadas."
            );
        }

        receptorRepository.delete(
                receptor
        );
    }

    // =====================================================
    // PESQUISAR
    // =====================================================

    public List<Receptor> pesquisar(
            String termo) {

        if (termo == null
                || termo.isBlank()) {

            return listar();
        }

        return receptorRepository
                .pesquisar(
                        termo.trim()
                );
    }

    // =====================================================
    // PREENCHER / FORMATAR
    // =====================================================

    private void preencherReceptor(
            Receptor receptor,
            ReceptorRequestDTO dto) {

        /*
         * Nome em letras maiúsculas
         */
        receptor.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );

        receptor.setCpf(Textos.cpfDigitos(dto.getCpf()));

        /*
         * Endereço
         */
        if (dto.getEndereco() != null
                && !dto.getEndereco().isBlank()) {

            receptor.setEndereco(
                    dto.getEndereco().trim()
            );

        } else {

            receptor.setEndereco(null);
        }

        /*
         * Telefone
         */
        if (dto.getTelefone() != null
                && !dto.getTelefone().isBlank()) {

            receptor.setTelefone(
                    dto.getTelefone().trim()
            );

        } else {

            receptor.setTelefone(null);
        }

        /*
         * E-mail em letras minúsculas
         */
        if (dto.getEmail() != null
                && !dto.getEmail().isBlank()) {

            receptor.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase()
            );

        } else {

            receptor.setEmail(null);
        }

        receptor.setObservacao(Textos.opcional(dto.getObservacao()));
    }

    private void validarCpf(String cpf, Long idAtual) {
        if (cpf == null || cpf.isBlank()) {
            return;
        }

        String cpfLimpo = Textos.cpfDigitos(cpf);

        if (cpfLimpo == null || cpfLimpo.length() != 11) {
            throw new IllegalArgumentException(
                    "CPF inválido! Informe 11 dígitos."
            );
        }

        boolean duplicado = receptorRepository.findByCpfIsNotNull()
                .stream()
                .anyMatch(existente ->
                        !existente.getId().equals(idAtual)
                                && Textos.mesmoCpf(existente.getCpf(), cpfLimpo)
                );

        if (duplicado) {
            throw new IllegalArgumentException(
                    "Já existe um receptor cadastrado com este CPF."
            );
        }
    }
}