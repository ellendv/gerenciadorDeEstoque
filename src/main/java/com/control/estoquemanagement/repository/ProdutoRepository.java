package com.control.estoquemanagement.repository;

import com.control.estoquemanagement.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
