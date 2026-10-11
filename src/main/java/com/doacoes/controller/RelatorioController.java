package com.doacoes.controller;

import com.doacoes.dto.RelatorioMensalDTO;
import com.doacoes.service.RelatorioService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/mensal")
    public ResponseEntity<RelatorioMensalDTO> mensal(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes) {

        return ResponseEntity.ok(relatorioService.gerar(ano, mes));
    }

    @GetMapping("/mensal.csv")
    public ResponseEntity<byte[]> csv(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes) {

        RelatorioMensalDTO relatorio = relatorioService.gerar(ano, mes);
        return arquivo(
                relatorioService.exportarCsv(ano, mes),
                "text/csv; charset=UTF-8",
                nomeArquivo(relatorio, "csv")
        );
    }

    @GetMapping("/mensal.pdf")
    public ResponseEntity<byte[]> pdf(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes) {

        RelatorioMensalDTO relatorio = relatorioService.gerar(ano, mes);
        return arquivo(
                relatorioService.exportarPdf(ano, mes),
                MediaType.APPLICATION_PDF_VALUE,
                nomeArquivo(relatorio, "pdf")
        );
    }

    private ResponseEntity<byte[]> arquivo(byte[] conteudo, String tipo, String nome) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, tipo);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(nome, StandardCharsets.UTF_8)
                        .build()
        );
        return ResponseEntity.ok().headers(headers).body(conteudo);
    }

    private String nomeArquivo(RelatorioMensalDTO relatorio, String extensao) {
        return String.format(
                "relatorio-solidario-%d-%02d.%s",
                relatorio.getAno(),
                relatorio.getMes(),
                extensao
        );
    }
}
