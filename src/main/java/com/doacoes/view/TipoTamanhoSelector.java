package com.doacoes.view;

import com.doacoes.enums.TipoTamanho;

import javax.swing.*;

public class TipoTamanhoSelector {

    public static TipoTamanho selecionar() {

        int opcao = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - BEBE
                    2 - CRIANCA
                    3 - ADULTO
                    4 - CALCADO
                    """)
        );

        return switch (opcao) {
            case 1 -> TipoTamanho.BEBE;
            case 2 -> TipoTamanho.CRIANCA;
            case 3 -> TipoTamanho.ADULTO;
            case 4 -> TipoTamanho.CALCADO;
            default -> null;
        };
    }
}
