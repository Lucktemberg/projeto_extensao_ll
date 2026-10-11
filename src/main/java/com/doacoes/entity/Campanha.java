package com.doacoes.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campanha")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "meta_quantidade", nullable = false)
    private Integer metaQuantidade;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(nullable = false)
    private Boolean ativa = true;

    @OneToMany(mappedBy = "campanha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampanhaItem> itens = new ArrayList<>();

    public Campanha() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMetaQuantidade() {
        return metaQuantidade;
    }

    public void setMetaQuantidade(Integer metaQuantidade) {
        this.metaQuantidade = metaQuantidade;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<CampanhaItem> getItens() {
        return itens;
    }

    public void setItens(List<CampanhaItem> itens) {
        this.itens = itens;
    }

    public void substituirItens(List<CampanhaItem> novosItens) {
        this.itens.clear();
        if (novosItens == null) {
            return;
        }

        for (CampanhaItem item : novosItens) {
            item.setCampanha(this);
            this.itens.add(item);
        }
    }
}
