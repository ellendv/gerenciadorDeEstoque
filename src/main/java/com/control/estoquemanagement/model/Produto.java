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

    @ManyToOne
    @JoinColumn(name = "PRODUTO_ESTOQUE_ID")
    private Estoque estoque;
}
