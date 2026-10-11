package com.doacoes.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "doacao")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_doador", nullable = false)
    private String nomeDoador;

    @Column(name = "doador_id")
    private Long doadorId;

    @Column(name = "descricao_item", nullable = false)
    private String descricaoItem;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "detalhe_unidade")
    private String detalheUnidade;

    private String tamanho;

    @Column(name = "data_doacao", nullable = false)
    private LocalDate dataDoacao;

    @Column(length = 1000)
    private String observacao;

    @Column(name = "status_acompanhamento")
    private String status;

    @Column(name = "codigo_acompanhamento", unique = true, length = 20)
    private String codigoAcompanhamento;

    @Column(name = "data_coleta")
    private LocalDate dataColeta;

    @Column(name = "campanha_id")
    private Long campanhaId;

    public Doacao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDetalheUnidade() {
        return detalheUnidade;
    }

    public void setDetalheUnidade(String detalheUnidade) {
        this.detalheUnidade = detalheUnidade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(LocalDate dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigoAcompanhamento() {
        return codigoAcompanhamento;
    }

    public void setCodigoAcompanhamento(String codigoAcompanhamento) {
        this.codigoAcompanhamento = codigoAcompanhamento;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }
}