package com.control.estoquemanagement.model.dto;

public class ProdutoDto {
    private String descricao;

    private Double valor;

    private Integer estoqueMinimo;

    private Integer estoqueAtual;

    public ProdutoDto() {
    }

    public ProdutoDto(String descricao, Double valor, Integer estoqueMinimo, Integer estoqueAtual) {
        this.descricao = descricao;
        this.valor = valor;
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
