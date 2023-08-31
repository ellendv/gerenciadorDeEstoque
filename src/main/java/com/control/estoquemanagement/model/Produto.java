package com.control.estoquemanagement.model;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "PRODUTO")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUTO_ID")
    private Long id;

    @Column(name = "PRODUTO_DESCRICAO")
    private String descricao;

    @Column(name = "PRODUTO_VALOR")
    private Double valor;

    @Column(name = "PRODUTO_DATA")
    private Date data;

    @Column(name = "PRODUTO_QTD_MINIMA")
    private Integer estoqueMinimo;

    @Column(name = "PRODUTO_QTD_ATUAL")
    private Integer estoqueAtual;


    public Produto(String descricao, Double valor, Integer estoqueMinimo, Integer estoqueAtual) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = new Date();
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueAtual = estoqueAtual;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }
}
