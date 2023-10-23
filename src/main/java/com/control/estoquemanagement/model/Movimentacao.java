package com.control.estoquemanagement.model;

import com.control.estoquemanagement.model.Enum.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "MOVIMENTACAO")
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MOVIMENTACAO_TIPO")
    private int tipo;

    @Column(name = "MOVIMENTACAO_DATA")
    private LocalDate data;

    @Column(name = "MOVIMENTACAO_QTD")
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "MOVIMENTACAO_IDPRODUTO")
    private Produto produto;

    public Movimentacao(int tipo, LocalDate data, int quantidade, Produto produto) {
        this.tipo = tipo;
        this.data = data;
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public Movimentacao() {
    }
}
