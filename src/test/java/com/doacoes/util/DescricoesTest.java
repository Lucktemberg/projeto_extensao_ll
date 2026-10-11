package com.doacoes.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DescricoesTest {

    @Test
    void deveNormalizarEspacosDuplicatasESufixoAdulto() {
        assertEquals(
                "CAMISETA - TAM G",
                Descricoes.normalizar("  camiseta   -  TAM  G - ADULTO ")
        );
        assertEquals(
                "ARROZ - PACOTE",
                Descricoes.normalizar("arroz - PACOTE PACOTE")
        );
    }

    @Test
    void deveConsiderarDescricoesEquivalentes() {
        assertTrue(Descricoes.equivalentes(
                "CAMISETA - TAM G - ADULTO",
                "camiseta - tam g"
        ));
        assertEquals(
                "VESTUARIO|CAMISETA - TAM G",
                Descricoes.chave("CAMISETA - TAM G - ADULTO", "vestuario")
        );
    }
}
