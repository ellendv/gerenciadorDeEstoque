package com.control.estoquemanagement.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ESTOQUE")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTOQUE_ID")
    private Long id;
    @Column(name = "ESTOQUE_DESCRICAO")
    private String descricao;
    @Column(name = "ESTOQUE_IDPRODUTO")
    private Long idProduto;
    @Column(name = "ESTOQUE_STATUS_PRODUTO")
    private String status;
}
