package com.control.estoquemanagement.model.dto;

import lombok.Data;

import java.util.Date;
@Data
public class ProdutoDto {

    private String codBarras;
    private String descricao;
    private Double valor;
    private Integer estoqueMinimo;
    private Integer estoqueAtual;
    private Double precoCusto;
    private Double precoVenda;
    private Date dataEntradaEstoque;

    public ProdutoDto() {
    }

    public ProdutoDto(String codBarras, String descricao, Double valor, Integer estoqueMinimo, Integer estoqueAtual, Double precoCusto, Double precoVenda, Date dataEntradaEstoque) {
        this.codBarras = codBarras;
        this.descricao = descricao;
        this.valor = valor;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueAtual = estoqueAtual;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.dataEntradaEstoque = dataEntradaEstoque;
    }
}
