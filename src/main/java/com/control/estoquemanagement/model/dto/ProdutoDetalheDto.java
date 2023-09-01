package com.control.estoquemanagement.model.dto;

import lombok.Data;

@Data
public class ProdutoDetalheDto {
    private String descricao;

    private Integer estoqueAtual;

    private String status;

}



