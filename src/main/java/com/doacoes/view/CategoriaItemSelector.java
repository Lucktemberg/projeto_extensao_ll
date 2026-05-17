package com.doacoes.view;

import com.doacoes.enums.CategoriaItem;

import javax.swing.*;

public class CategoriaItemSelector {

    public static CategoriaItem selecionar() {

        int opcao = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - VESTUARIO
                    2 - ALIMENTOS
                    """)
        );

        return switch (opcao) {
            case 1 -> CategoriaItem.VESTUARIO;
            case 2 -> CategoriaItem.ALIMENTOS;
            default -> null;
        };
    }
}
