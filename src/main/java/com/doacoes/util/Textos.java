package com.doacoes.util;

public final class Textos {

    private Textos() {
    }

    public static String opcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    public static String cpfDigitos(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }

        String digitos = cpf.replaceAll("[^0-9]", "");
        return digitos.isBlank() ? null : digitos;
    }

    public static boolean mesmoCpf(String primeiro, String segundo) {
        String a = cpfDigitos(primeiro);
        String b = cpfDigitos(segundo);
        return a != null && a.equals(b);
    }
}
