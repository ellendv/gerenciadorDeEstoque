package com.control.estoquemanagement.repository;

import com.control.estoquemanagement.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findProdutoByDescricao(@Param("descricao") String descricao);

    Produto findProdutoByCodBarras(@Param("codBarras")String codBarras);
}
