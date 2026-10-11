package com.doacoes.service;

import com.doacoes.dto.ItemQuantidadeDTO;
import com.doacoes.dto.RelatorioMensalDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

final class RelatorioExportacao {

    private RelatorioExportacao() {
    }

    static byte[] csv(RelatorioMensalDTO relatorio) {
        StringBuilder csv = new StringBuilder();
        csv.append("Relatório mensal Solidário\n");
        csv.append("Competência;").append(texto(relatorio.getCompetencia())).append('\n');
        csv.append("Doações no mês;").append(relatorio.getTotalDoacoes()).append('\n');
        csv.append("Itens doados;").append(relatorio.getItensDoados()).append('\n');
        csv.append("Distribuições;").append(relatorio.getTotalDistribuicoes()).append('\n');
        csv.append("Itens distribuídos;").append(relatorio.getItensDistribuidos()).append('\n');
        csv.append('\n');
        csv.append("Por etapa;Quantidade\n");
        escreverLinhas(csv, relatorio.getPorStatus());
        csv.append('\n');
        csv.append("Por categoria;Quantidade\n");
        escreverLinhas(csv, relatorio.getPorCategoria());
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    static byte[] pdf(RelatorioMensalDTO relatorio) {
        try {
            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4, 48, 48, 52, 48);
            PdfWriter.getInstance(documento, saida);
            documento.open();

            BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            Font titulo = new Font(base, 18, Font.BOLD, new Color(23, 116, 95));
            Font subtitulo = new Font(base, 12, Font.NORMAL, new Color(80, 90, 98));
            Font secao = new Font(base, 13, Font.BOLD, new Color(32, 40, 46));
            Font normal = new Font(base, 11, Font.NORMAL, new Color(32, 40, 46));
            Font cabecalho = new Font(base, 11, Font.BOLD, Color.WHITE);

            documento.add(new Paragraph("Solidário", titulo));
            documento.add(new Paragraph("Relatório mensal", subtitulo));
            documento.add(new Paragraph(texto(relatorio.getCompetencia()), secao));
            documento.add(new Paragraph(" ", normal));

            PdfPTable totais = new PdfPTable(2);
            totais.setWidthPercentage(100);
            totais.setWidths(new float[]{3f, 1.2f});
            adicionarLinha(totais, "Doações no mês", relatorio.getTotalDoacoes(), normal);
            adicionarLinha(totais, "Itens doados", relatorio.getItensDoados(), normal);
            adicionarLinha(totais, "Distribuições", relatorio.getTotalDistribuicoes(), normal);
            adicionarLinha(totais, "Itens distribuídos", relatorio.getItensDistribuidos(), normal);
            documento.add(totais);

            documento.add(new Paragraph(" ", normal));
            documento.add(new Paragraph("Por etapa", secao));
            documento.add(tabela(relatorio.getPorStatus(), cabecalho, normal));

            documento.add(new Paragraph(" ", normal));
            documento.add(new Paragraph("Por categoria", secao));
            documento.add(tabela(relatorio.getPorCategoria(), cabecalho, normal));

            documento.close();
            return saida.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Não foi possível gerar o PDF do relatório", exception);
        }
    }

    private static void escreverLinhas(StringBuilder csv, List<ItemQuantidadeDTO> itens) {
        if (itens == null || itens.isEmpty()) {
            csv.append("Nenhum registro;0\n");
            return;
        }
        itens.forEach(item -> csv.append(texto(item.getNome()))
                .append(';')
                .append(item.getQuantidade())
                .append('\n'));
    }

    private static PdfPTable tabela(List<ItemQuantidadeDTO> itens, Font cabecalho, Font normal) {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(8);
        tabela.setWidths(new float[]{3f, 1.2f});
        tabela.addCell(celula("Descrição", cabecalho, new Color(23, 116, 95), true));
        tabela.addCell(celula("Quantidade", cabecalho, new Color(23, 116, 95), true));

        if (itens == null || itens.isEmpty()) {
            tabela.addCell(celula("Nenhum registro", normal, Color.WHITE, false));
            tabela.addCell(celula("0", normal, Color.WHITE, true));
            return tabela;
        }

        itens.forEach(item -> {
            tabela.addCell(celula(texto(item.getNome()), normal, Color.WHITE, false));
            tabela.addCell(celula(String.valueOf(item.getQuantidade()), normal, Color.WHITE, true));
        });
        return tabela;
    }

    private static void adicionarLinha(PdfPTable tabela, String rotulo, long valor, Font fonte) {
        tabela.addCell(celula(rotulo, fonte, new Color(245, 248, 247), false));
        tabela.addCell(celula(String.valueOf(valor), fonte, new Color(245, 248, 247), true));
    }

    private static PdfPCell celula(String texto, Font fonte, Color fundo, boolean direita) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setBackgroundColor(fundo);
        celula.setPadding(8);
        celula.setHorizontalAlignment(direita ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        celula.setBorderColor(new Color(214, 222, 218));
        return celula;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor;
    }
}
