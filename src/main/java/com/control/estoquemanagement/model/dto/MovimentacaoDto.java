package com.control.estoquemanagement.model.dto;

import com.control.estoquemanagement.model.Enum.TipoMovimentacao;
import lombok.Data;

@Data
public class MovimentacaoDto {
    private Long produtoId;
    private TipoMovimentacao tipo;
    private int quantidade;
}
