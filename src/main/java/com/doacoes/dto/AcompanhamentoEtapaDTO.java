package com.doacoes.dto;

public class AcompanhamentoEtapaDTO {

    private final String chave;
    private final String titulo;
    private final boolean concluida;
    private final boolean atual;

    public AcompanhamentoEtapaDTO(
            String chave,
            String titulo,
            boolean concluida,
            boolean atual) {

        this.chave = chave;
        this.titulo = titulo;
        this.concluida = concluida;
        this.atual = atual;
    }

    public String getChave() {
        return chave;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public boolean isAtual() {
        return atual;
    }
}
