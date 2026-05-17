package com.doacoes.view;

import javax.swing.*;

public class MenuView {

    private final TipoDoacaoView tipoDoacaoView = new TipoDoacaoView();
    private final TamanhoRoupaView tamanhoRoupaView = new TamanhoRoupaView();

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
                case 2 -> menuTamanhoRoupa();
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

                case 1 -> tipoDoacaoView.cadastrar();
                case 2 -> tipoDoacaoView.listar();
                case 3 -> tipoDoacaoView.atualizar();
                case 4 -> tipoDoacaoView.deletar();
                case 0 -> {}
                default -> JOptionPane.showMessageDialog(null, "Opção inválida");
            }

        } while (opcao != 0);
    }

    // ==================================================
    // MENU TAMANHO ROUPA
    // ==================================================

    private void menuTamanhoRoupa() {

        int opcao;

        do {

            opcao = Integer.parseInt(
                    JOptionPane.showInputDialog("""

                            ===== TAMANHO ROUPA =====

                            1 - Cadastrar
                            2 - Listar
                            3 - Atualizar
                            4 - Deletar
                            0 - Voltar
                            """)
            );

            switch (opcao) {

                case 1 -> tamanhoRoupaView.cadastrar();
                case 2 -> tamanhoRoupaView.listar();
                case 3 -> tamanhoRoupaView.atualizar();
                case 4 -> tamanhoRoupaView.deletar();
                case 0 -> {}
                default -> JOptionPane.showMessageDialog(null, "Opção inválida");
            }

        } while (opcao != 0);
    }
}