package com.doacoes.view;

import com.doacoes.enums.TipoTamanho;
import com.doacoes.model.TamanhoRoupa;
import com.doacoes.service.TamanhoRoupaService;

import javax.swing.*;

public class TamanhoRoupaView {

    private final TamanhoRoupaService tamanhoService = new TamanhoRoupaService();

    public void cadastrar() {

        String tamanho = JOptionPane.showInputDialog("Tamanho (ex: P, M, G, 38):").toUpperCase();

        TipoTamanho categoria = TipoTamanhoSelector.selecionar();

        if (categoria == null) {
            JOptionPane.showMessageDialog(null, "Categoria inválida");
            return;
        }

        TamanhoRoupa tamanhoRoupa = new TamanhoRoupa();

        tamanhoRoupa.setTamanho(tamanho);
        tamanhoRoupa.setCategoria(categoria);

        boolean salvou = tamanhoService.salvar(tamanhoRoupa);

        if (salvou) {
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar");
        }
    }

    public void listar() {

        StringBuilder sb = new StringBuilder();

        tamanhoService.listar().forEach(item ->
                sb.append(item).append("\n")
        );

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void atualizar() {

        Long id = Long.parseLong(
                JOptionPane.showInputDialog("Digite o ID:")
        );

        TamanhoRoupa tamanhoRoupa = tamanhoService.buscarPorId(id);

        if (tamanhoRoupa == null) {
            JOptionPane.showMessageDialog(null, "Registro não encontrado");
            return;
        }

        String novoTamanho = JOptionPane.showInputDialog(
                "Novo tamanho:",
                tamanhoRoupa.getTamanho()
        ).toUpperCase();

        TipoTamanho categoria = TipoTamanhoSelector.selecionar();

        if (categoria == null) {
            JOptionPane.showMessageDialog(null, "Categoria inválida");
            return;
        }

        tamanhoRoupa.setTamanho(novoTamanho);
        tamanhoRoupa.setCategoria(categoria);

        boolean atualizou = tamanhoService.atualizar(tamanhoRoupa);

        if (atualizou) {
            JOptionPane.showMessageDialog(null, "Atualizado com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar");
        }
    }

    public void deletar() {

        Long id = Long.parseLong(
                JOptionPane.showInputDialog("Digite o ID:")
        );

        TamanhoRoupa tamanhoRoupa = tamanhoService.buscarPorId(id);

        if (tamanhoRoupa == null) {
            JOptionPane.showMessageDialog(null, "Tamanho não encontrado!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + tamanhoRoupa.getId() + "\n" +
                        "Tamanho: " + tamanhoRoupa.getTamanho() + "\n" +
                        "Categoria: " + tamanhoRoupa.getCategoria(),
                "Confirmar exclusão",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacao == JOptionPane.OK_OPTION) {
            tamanhoService.deletar(id);
            JOptionPane.showMessageDialog(null, "Deletado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Exclusão cancelada.");
        }
    }
}
