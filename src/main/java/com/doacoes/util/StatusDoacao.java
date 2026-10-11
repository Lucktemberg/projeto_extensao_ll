package com.doacoes.util;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class StatusDoacao {

    public static final String CADASTRADA = "CADASTRADA";
    public static final String COLETA_AGENDADA = "COLETA_AGENDADA";
    public static final String RECEBIDA = "RECEBIDA";
    public static final String DISTRIBUIDA = "DISTRIBUIDA";

    public static final List<String> ETAPAS = List.of(
            CADASTRADA,
            COLETA_AGENDADA,
            RECEBIDA,
            DISTRIBUIDA
    );

    private static final Set<String> ESTOQUE = Set.of(RECEBIDA, DISTRIBUIDA);

    private StatusDoacao() {
    }

    public static String normalizar(String status) {
        if (status == null || status.isBlank()) {
            return RECEBIDA;
        }

        return status.trim().toUpperCase(Locale.ROOT);
    }

    public static String rotulo(String status) {
        return switch (normalizar(status)) {
            case CADASTRADA -> "Cadastrada";
            case COLETA_AGENDADA -> "Coleta agendada";
            case RECEBIDA -> "Recebida";
            case DISTRIBUIDA -> "Distribuída";
            default -> "Recebida";
        };
    }

    public static boolean contabilizaEstoque(String status) {
        return ESTOQUE.contains(normalizar(status));
    }

    public static boolean contabilizaCampanha(String status) {
        return contabilizaEstoque(status);
    }

    public static boolean transicaoPermitida(String origem, String destino) {
        String de = normalizar(origem);
        String para = normalizar(destino);

        if (de.equals(para)) {
            return true;
        }

        return switch (de) {
            case CADASTRADA -> COLETA_AGENDADA.equals(para) || RECEBIDA.equals(para);
            case COLETA_AGENDADA -> CADASTRADA.equals(para) || RECEBIDA.equals(para);
            case RECEBIDA -> COLETA_AGENDADA.equals(para) || DISTRIBUIDA.equals(para);
            case DISTRIBUIDA -> RECEBIDA.equals(para);
            default -> false;
        };
    }
}
