package com.control.estoquemanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "PRODUTO")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUTO_DESCRICAO")
    private String descricao;

    @Column(name = "PRODUTO_VALOR")
    private Double valor;

    @Column(name = "PRODUTO_QTD_MINIMA")
    private Integer estoqueMinimo;

    @Column(name = "PRODUTO_QTD_ATUAL")
    private Integer estoqueAtual;

    @Column(name = "COD_BARRAS")
    private String codBarras;

    @Column(name = "PRECO_CUSTO")
    private Double precoCusto;

    @Column(name = "PRECO_VENDA")
    private Double precoVenda;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ENTRADA_ESTOQUE")
    private Date dataEntradaEstoque;

    public Produto(String descricao, Double valor, Integer estoqueMinimo, Integer estoqueAtual, String codBarras, Double precoCusto, Double precoVenda, Date dataEntradaEstoque) {
        this.descricao = descricao;
        this.valor = valor;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueAtual = estoqueAtual;
        this.codBarras = codBarras;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.dataEntradaEstoque = dataEntradaEstoque;
    }

    public Produto() {
    }


}
