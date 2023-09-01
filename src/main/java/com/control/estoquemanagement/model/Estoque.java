package com.control.estoquemanagement.model;

import com.control.estoquemanagement.model.Enum.SituacaoEstoque;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "ESTOQUE")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTOQUE_ID")
    private Long id;
    @Column(name = "ESTOQUE_IDPRODUTO")
    private Long idProduto;
    @Column(name = "ESTOQUE_SITUACAO_PRODUTO")
    private String estoque;


    public Estoque() {
    }

    public Estoque(Long idProduto, String estoque) {
        this.idProduto = idProduto;
        this.estoque = estoque;
    }


}
