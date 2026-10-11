package com.doacoes.util;

import java.util.Locale;

public final class Descricoes {

    private Descricoes() {
    }

    public static String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }

        String texto = valor
                .trim()
                .replaceAll("\\s+", " ")
                .toUpperCase(Locale.ROOT);

        texto = texto.replaceAll("\\b(PACOTE)(?:\\s+\\1)+\\b", "$1");
        texto = texto.replaceAll("\\s*-\\s*ADULTO\\b", "");
        texto = texto.replaceAll("\\s{2,}", " ").trim();
        texto = texto.replaceAll("\\s+-\\s*$", "");

        return texto;
    }

    public static boolean equivalentes(String primeira, String segunda) {
        return normalizar(primeira).equals(normalizar(segunda));
    }

    public static String chave(String descricao, String categoria) {
        return normalizar(categoria) + "|" + normalizar(descricao);
    }
}
