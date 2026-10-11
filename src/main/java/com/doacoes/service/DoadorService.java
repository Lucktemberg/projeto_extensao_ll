package com.doacoes.service;

import com.doacoes.dto.DoadorRequestDTO;
import com.doacoes.entity.Doador;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.repository.DoadorRepository;
import com.doacoes.util.Textos;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoadorService {

    private final DoadorRepository doadorRepository;
    private final DoacaoRepository doacaoRepository;

    public DoadorService(
            DoadorRepository doadorRepository,
            DoacaoRepository doacaoRepository) {
        this.doadorRepository = doadorRepository;
        this.doacaoRepository = doacaoRepository;
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public Doador salvar(DoadorRequestDTO dto) {

        validarCpf(dto.getCpf(), null);

        Doador doador = new Doador();

        preencherDoador(doador, dto);

        return doadorRepository.save(doador);
    }

    // =========================
    // READ - LISTAR
    // =========================

    public List<Doador> listar() {
        return doadorRepository.findAllByOrderByNomeAsc();
    }

    // =========================
    // READ - BUSCAR POR ID
    // =========================

    public Doador buscarPorId(Long id) {

        return doadorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Doador não encontrado"
                        ));
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public Doador atualizar(
            Long id,
            DoadorRequestDTO dto) {

        validarCpf(dto.getCpf(), id);

        Doador doador = buscarPorId(id);

        preencherDoador(doador, dto);

        return doadorRepository.save(doador);
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deletar(Long id) {

        Doador doador = buscarPorId(id);

        if (doacaoRepository.existsByDoadorId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este doador porque já existem doações vinculadas."
            );
        }

        doadorRepository.delete(doador);
    }

    // =========================
    // PESQUISAR
    // =========================

    public List<Doador> pesquisar(String termo) {

        if (termo == null || termo.isBlank()) {
            return listar();
        }

        return doadorRepository.pesquisar(
                termo.trim()
        );
    }

    // =========================
    // PREENCHER / FORMATAR
    // =========================

    private void preencherDoador(
            Doador doador,
            DoadorRequestDTO dto) {

        // Nome em maiúsculo
        doador.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );

        doador.setCpf(Textos.cpfDigitos(dto.getCpf()));

        // Endereço
        if (dto.getEndereco() != null
                && !dto.getEndereco().isBlank()) {

            doador.setEndereco(
                    dto.getEndereco().trim()
            );

        } else {
            doador.setEndereco(null);
        }

        // Telefone
        if (dto.getTelefone() != null
                && !dto.getTelefone().isBlank()) {

            doador.setTelefone(
                    dto.getTelefone().trim()
            );

        } else {
            doador.setTelefone(null);
        }

        // E-mail em minúsculo
        if (dto.getEmail() != null
                && !dto.getEmail().isBlank()) {

            doador.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase()
            );

        } else {
            doador.setEmail(null);
        }

        doador.setObservacao(Textos.opcional(dto.getObservacao()));
    }

    // =========================
    // VALIDAR CPF
    // =========================

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

        boolean duplicado = doadorRepository.findByCpfIsNotNull()
                .stream()
                .anyMatch(existente ->
                        !existente.getId().equals(idAtual)
                                && Textos.mesmoCpf(existente.getCpf(), cpfLimpo)
                );

        if (duplicado) {
            throw new IllegalArgumentException(
                    "Já existe um doador cadastrado com este CPF."
            );
        }
    }
}