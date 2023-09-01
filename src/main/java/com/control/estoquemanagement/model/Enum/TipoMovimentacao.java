package com.control.estoquemanagement.model.Enum;

public enum TipoMovimentacao {
    ENTRADA(1), SAIDA(2);

    private final int valor;

    TipoMovimentacao(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
