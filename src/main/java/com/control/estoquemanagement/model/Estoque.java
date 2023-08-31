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
    @Column(name = "ESTOQUE_PRODUTO")
    private Long listProdutos;
}
