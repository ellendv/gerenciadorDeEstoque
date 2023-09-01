package com.control.estoquemanagement.repository;

import com.control.estoquemanagement.model.Movimentacao;
import com.control.estoquemanagement.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByProdutoAndTipoAndDataBetween(Produto produto, int tipo, LocalDate dataInicial, LocalDate dataFinal);

    List<Movimentacao> findAllByTipoAndDataBetween(int valor, LocalDate dataInicial, LocalDate dataFinal);

    List<Movimentacao> findByTipoAndDataBetween(int valor, LocalDate dataInicial, LocalDate dataFinal);
}
