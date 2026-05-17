package com.doacoes.view;

import com.doacoes.enums.CategoriaItem;
import com.doacoes.model.TipoDoacao;
import com.doacoes.service.TipoDoacaoService;

import javax.swing.*;

public class TipoDoacaoView {

    private final TipoDoacaoService tipoService = new TipoDoacaoService();

    public void cadastrar() {

        String nome = JOptionPane.showInputDialog("Nome:").toUpperCase();

        CategoriaItem categoria = CategoriaItemSelector.selecionar();

        if (categoria == null) {
            JOptionPane.showMessageDialog(null, "Categoria inválida");
            return;
        }

        TipoDoacao tipo = new TipoDoacao();

        tipo.setNome(nome);
        tipo.setCategoria(categoria);

        boolean salvou = tipoService.salvar(tipo);

        if (salvou) {
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar");
        }
    }

    public void listar() {

        StringBuilder sb = new StringBuilder();

        tipoService.listar().forEach(item ->
                sb.append(item).append("\n")
        );

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void atualizar() {

        Long id = Long.parseLong(
                JOptionPane.showInputDialog("Digite o ID:")
        );

        TipoDoacao tipo = tipoService.buscarPorId(id);

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Registro não encontrado");
            return;
        }

        String novoNome = JOptionPane.showInputDialog(
                "Novo nome:",
                tipo.getNome()
        );

        CategoriaItem categoria = CategoriaItemSelector.selecionar();

        if (categoria == null) {
            JOptionPane.showMessageDialog(null, "Categoria inválida");
            return;
        }

        tipo.setNome(novoNome);
        tipo.setCategoria(categoria);

        boolean atualizou = tipoService.atualizar(tipo);

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

        TipoDoacao tipo = tipoService.buscarPorId(id);

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Tipo de doação não encontrado!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + tipo.getId() + "\n" +
                        "Nome: " + tipo.getNome(),
                "Confirmar exclusão",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacao == JOptionPane.OK_OPTION) {
            tipoService.deletar(id);
            JOptionPane.showMessageDialog(null, "Deletado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Exclusão cancelada.");
        }
    }
}
