package com.doacoes.service;

import com.doacoes.dto.ItemQuantidadeDTO;
import com.doacoes.dto.RelatorioMensalDTO;
import com.doacoes.entity.Distribuicao;
import com.doacoes.entity.Doacao;
import com.doacoes.repository.DistribuicaoRepository;
import com.doacoes.repository.DoacaoRepository;
import com.doacoes.util.StatusDoacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private static final DateTimeFormatter COMPETENCIA =
            DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.forLanguageTag("pt-BR"));

    private final DoacaoRepository doacaoRepository;
    private final DistribuicaoRepository distribuicaoRepository;

    public RelatorioService(
            DoacaoRepository doacaoRepository,
            DistribuicaoRepository distribuicaoRepository) {

        this.doacaoRepository = doacaoRepository;
        this.distribuicaoRepository = distribuicaoRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioMensalDTO gerar(Integer ano, Integer mes) {
        YearMonth periodo = resolverPeriodo(ano, mes);
        LocalDate inicio = periodo.atDay(1);
        LocalDate fim = periodo.atEndOfMonth();

        List<Doacao> doacoes = doacaoRepository.findByDataDoacaoBetween(inicio, fim);
        List<Distribuicao> distribuicoes = distribuicaoRepository.findByDataBetween(
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );

        long itensDoados = doacoes.stream()
                .mapToLong(doacao -> doacao.getQuantidade() == null ? 0 : doacao.getQuantidade())
                .sum();

        long itensDistribuidos = distribuicoes.stream()
                .mapToLong(distribuicao -> distribuicao.getQuantidade() == null ? 0 : distribuicao.getQuantidade())
                .sum();

        Map<String, Long> status = doacoes.stream()
                .collect(Collectors.groupingBy(
                        doacao -> StatusDoacao.rotulo(doacao.getStatus()),
                        Collectors.counting()
                ));

        Map<String, Long> categorias = doacoes.stream()
                .collect(Collectors.groupingBy(
                        doacao -> rotuloCategoria(doacao.getCategoria()),
                        Collectors.counting()
                ));

        return montar(periodo, doacoes, distribuicoes, itensDoados, itensDistribuidos, status, categorias);
    }

    public byte[] exportarCsv(Integer ano, Integer mes) {
        return RelatorioExportacao.csv(gerar(ano, mes));
    }

    public byte[] exportarPdf(Integer ano, Integer mes) {
        return RelatorioExportacao.pdf(gerar(ano, mes));
    }

    private RelatorioMensalDTO montar(
            java.time.YearMonth periodo,
            List<Doacao> doacoes,
            List<Distribuicao> distribuicoes,
            long itensDoados,
            long itensDistribuidos,
            Map<String, Long> status,
            Map<String, Long> categorias) {

        return new RelatorioMensalDTO(
                periodo.getYear(),
                periodo.getMonthValue(),
                capitalize(periodo.format(COMPETENCIA)),
                doacoes.size(),
                itensDoados,
                distribuicoes.size(),
                itensDistribuidos,
                paraLista(status),
                paraLista(categorias)
        );
    }

    private YearMonth resolverPeriodo(Integer ano, Integer mes) {
        YearMonth atual = YearMonth.now();
        int anoUsado = ano == null ? atual.getYear() : ano;
        int mesUsado = mes == null ? atual.getMonthValue() : mes;

        if (mesUsado < 1 || mesUsado > 12) {
            throw new IllegalArgumentException("Mês inválido");
        }

        return YearMonth.of(anoUsado, mesUsado);
    }

    private List<ItemQuantidadeDTO> paraLista(Map<String, Long> valores) {
        return valores.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .map(entrada -> new ItemQuantidadeDTO(entrada.getKey(), entrada.getValue()))
                .toList();
    }

    private String rotuloCategoria(String categoria) {
        String chave = categoria == null ? "" : categoria.trim().toUpperCase(Locale.ROOT);
        return switch (chave) {
            case "ALIMENTOS" -> "Alimentos";
            case "VESTUARIO" -> "Vestuário";
            default -> chave.isBlank() ? "Outros" : chave;
        };
    }

    private String capitalize(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }
        return texto.substring(0, 1).toUpperCase(Locale.forLanguageTag("pt-BR")) + texto.substring(1);
    }
}
