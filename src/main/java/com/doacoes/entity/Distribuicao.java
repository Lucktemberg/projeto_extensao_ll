package com.doacoes.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "distribuicao")
public class Distribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estoque_id", nullable = false)
    private Long estoqueId;

    @Column(name = "receptor_id", nullable = false)
    private Long receptorId;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(length = 1000)
    private String observacao;

    public Distribuicao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(Long estoqueId) {
        this.estoqueId = estoqueId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
