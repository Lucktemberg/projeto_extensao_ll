package com.doacoes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "campanha_item")
public class CampanhaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campanha_id", nullable = false)
    private Campanha campanha;

    @Column(name = "descricao_item", nullable = false)
    private String descricaoItem;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "meta_quantidade", nullable = false)
    private Integer metaQuantidade;

    public CampanhaItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getMetaQuantidade() {
        return metaQuantidade;
    }

    public void setMetaQuantidade(Integer metaQuantidade) {
        this.metaQuantidade = metaQuantidade;
    }
}
