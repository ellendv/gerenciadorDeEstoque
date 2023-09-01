package com.control.estoquemanagement.model;

import com.control.estoquemanagement.model.Enum.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private LocalDate data;
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Movimentacao(TipoMovimentacao tipo, LocalDate data, int quantidade, Produto produto) {
        this.tipo = tipo;
        this.data = data;
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public Movimentacao() {
    }
}
