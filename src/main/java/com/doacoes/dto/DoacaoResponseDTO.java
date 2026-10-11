package com.doacoes.dto;

import com.doacoes.entity.Doacao;
import com.doacoes.util.StatusDoacao;

import java.time.LocalDate;

public class DoacaoResponseDTO {

    private Long id;
    private Long doadorId;
    private String nomeDoador;
    private String descricaoItem;
    private Integer quantidade;
    private String categoria;
    private String unidade;
    private String detalheUnidade;
    private String tamanho;
    private LocalDate dataDoacao;
    private String observacao;
    private String status;
    private String statusRotulo;
    private String codigoAcompanhamento;
    private LocalDate dataColeta;
    private Long campanhaId;
    private String campanhaNome;
    private String emailDoador;
    private String telefoneDoador;
    private String linkWhatsapp;
    private String linkEmail;

    public DoacaoResponseDTO(Doacao doacao) {
        this.id = doacao.getId();
        this.doadorId = doacao.getDoadorId();
        this.nomeDoador = doacao.getNomeDoador();
        this.descricaoItem = doacao.getDescricaoItem();
        this.quantidade = doacao.getQuantidade();
        this.categoria = doacao.getCategoria();
        this.unidade = doacao.getUnidade();
        this.detalheUnidade = doacao.getDetalheUnidade();
        this.tamanho = doacao.getTamanho();
        this.dataDoacao = doacao.getDataDoacao();
        this.observacao = doacao.getObservacao();
        this.status = StatusDoacao.normalizar(doacao.getStatus());
        this.statusRotulo = StatusDoacao.rotulo(doacao.getStatus());
        this.codigoAcompanhamento = doacao.getCodigoAcompanhamento();
        this.dataColeta = doacao.getDataColeta();
        this.campanhaId = doacao.getCampanhaId();
    }

    public Long getId() {
        return id;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getDetalheUnidade() {
        return detalheUnidade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusRotulo() {
        return statusRotulo;
    }

    public String getCodigoAcompanhamento() {
        return codigoAcompanhamento;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public String getCampanhaNome() {
        return campanhaNome;
    }

    public void setCampanhaNome(String campanhaNome) {
        this.campanhaNome = campanhaNome;
    }

    public String getEmailDoador() {
        return emailDoador;
    }

    public void setEmailDoador(String emailDoador) {
        this.emailDoador = emailDoador;
    }

    public String getTelefoneDoador() {
        return telefoneDoador;
    }

    public void setTelefoneDoador(String telefoneDoador) {
        this.telefoneDoador = telefoneDoador;
    }

    public String getLinkWhatsapp() {
        return linkWhatsapp;
    }

    public void setLinkWhatsapp(String linkWhatsapp) {
        this.linkWhatsapp = linkWhatsapp;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }
}