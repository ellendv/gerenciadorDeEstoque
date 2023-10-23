package com.control.estoquemanagement.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PRECO_HISTORICO")
public class PrecoHistorico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRECO_HISTORICO_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUTO_ID")
    private Produto produto;

    @Column(name = "PRECO_VENDA")
    private Double precoVenda;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDate dataAtualizacao;

    // Outros atributos, getters e setters
}

