package com.doacoes.view;

import com.doacoes.enums.CategoriaItem;
//import com.doacoes.enums.TipoTamanho;
//import com.doacoes.model.TamanhoRoupa;
import com.doacoes.model.TipoDoacao;
//import com.doacoes.service.TamanhoRoupaService;
import com.doacoes.service.TipoDoacaoService;

import javax.swing.*;

public class MenuView {

    private final TipoDoacaoService tipoService = new TipoDoacaoService();
   // private final TamanhoRoupaService tamanhoService = new TamanhoRoupaService();

    public void iniciar() {

        int opcao;

        do {

            opcao = Integer.parseInt(
                    JOptionPane.showInputDialog("""

                            ===== MENU PRINCIPAL =====

                            1 - Tipo Doação
                            2 - Tamanho Roupa
                            0 - Sair
                            """)
            );

            switch (opcao) {

                case 1 -> menuTipoDoacao();
               // case 2 -> menuTamanhoRoupa();
                case 0 -> JOptionPane.showMessageDialog(null, "Saindo...");
                default -> JOptionPane.showMessageDialog(null, "Opção inválida");
            }

        } while (opcao != 0);
    }

    // ==================================================
    // MENU TIPO DOAÇÃO
    // ==================================================

    private void menuTipoDoacao() {

        int opcao;

        do {

            opcao = Integer.parseInt(
                    JOptionPane.showInputDialog("""

                            ===== TIPO DOAÇÃO =====

                            1 - Cadastrar
                            2 - Listar
                            3 - Atualizar
                            4 - Deletar
                            0 - Voltar
                            """)
            );

            switch (opcao) {

                case 1 -> cadastrarTipo();
                case 2 -> listarTipos();
                case 3 -> atualizarTipo();
                case 4 -> deletarTipo();
                case 0 -> {}
                default -> JOptionPane.showMessageDialog(null, "Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrarTipo() {

        String nome = JOptionPane.showInputDialog("Nome:").toUpperCase();

        int opcaoCategoria = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - VESTUARIO
                    2 - ALIMENTOS
                    """)
        );

        CategoriaItem categoria;

        switch (opcaoCategoria) {

            case 1 -> categoria = CategoriaItem.VESTUARIO;
            case 2 -> categoria = CategoriaItem.ALIMENTOS;

            default -> {
                JOptionPane.showMessageDialog(null, "Categoria inválida");
                return;
            }
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

    private void listarTipos() {

        StringBuilder sb = new StringBuilder();

        tipoService.listar().forEach(item ->
                sb.append(item).append("\n")
        );

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void atualizarTipo() {

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

        int opcaoCategoria = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - VESTUARIO
                    2 - ALIMENTOS
                    """)
        );

        CategoriaItem categoria;

        switch (opcaoCategoria) {

            case 1 -> categoria = CategoriaItem.VESTUARIO;
            case 2 -> categoria = CategoriaItem.ALIMENTOS;

            default -> {
                JOptionPane.showMessageDialog(null, "Categoria inválida");
                return;
            }
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

    private void deletarTipo() {

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
    }}


