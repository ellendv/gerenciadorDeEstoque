package com.control.estoquemanagement.model.dto;

import lombok.Data;

@Data
public class ProdutoDetalheDto {
    private Long idProduto;

    private String descricao;

    private Integer estoqueAtual;

    private String status;

    public ProdutoDetalheDto() {
    }

    public ProdutoDetalheDto(Long idProduto, String descricao, Integer estoqueAtual, String status) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.estoqueAtual = estoqueAtual;
        this.status = status;
    }
}



