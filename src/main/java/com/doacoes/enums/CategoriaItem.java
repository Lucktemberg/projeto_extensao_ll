package com.doacoes.enums;

public enum CategoriaItem {

    VESTUARIO(1),
    ALIMENTOS(2);

    private int codigo;

    CategoriaItem(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static CategoriaItem porCodigo(int codigo) {
        for (CategoriaItem categoria : values()) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }

        throw new IllegalArgumentException("Código inválido!");
    }
}




