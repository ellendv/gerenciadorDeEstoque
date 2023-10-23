package com.control.estoquemanagement.model.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class MovimentacaoDetalheDto {
    private String tipoMovimentacao;
    private Integer quantidadeMovimentada;
    private LocalDate data;
    private ProdutoDetalheDto produto;



}
