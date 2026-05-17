package com.doacoes.service;

import com.doacoes.dao.TamanhoRoupaDAO;
import com.doacoes.model.TamanhoRoupa;

import java.util.List;

public class TamanhoRoupaService {

    private final TamanhoRoupaDAO tamanhoRoupaDAO = new TamanhoRoupaDAO();

    public boolean salvar(TamanhoRoupa tamanhoRoupa) {

        if (tamanhoRoupa.getTamanho() == null || tamanhoRoupa.getTamanho().isBlank()) {
            return false;
        }

        if (tamanhoRoupa.getCategoria() == null) {
            return false;
        }

        return tamanhoRoupaDAO.salvar(tamanhoRoupa);
    }

    public List<TamanhoRoupa> listar() {
        return tamanhoRoupaDAO.listar();
    }

    public TamanhoRoupa buscarPorId(long id) {
        return tamanhoRoupaDAO.buscarPorId(id);
    }

    public boolean atualizar(TamanhoRoupa tamanhoRoupa) {

        if (tamanhoRoupa.getTamanho() == null || tamanhoRoupa.getTamanho().isBlank()) {
            return false;
        }

        if (tamanhoRoupa.getCategoria() == null) {
            return false;
        }

        TamanhoRoupa existente = tamanhoRoupaDAO.buscarPorId(tamanhoRoupa.getId());

        if (existente == null) {
            return false;
        }

        return tamanhoRoupaDAO.atualizar(tamanhoRoupa);
    }

    public boolean deletar(long id) {

        TamanhoRoupa existente = tamanhoRoupaDAO.buscarPorId(id);

        if (existente == null) {
            return false;
        }

        return tamanhoRoupaDAO.deletar(id);
    }
}