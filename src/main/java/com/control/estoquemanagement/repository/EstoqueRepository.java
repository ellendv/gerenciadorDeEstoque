package com.control.estoquemanagement.repository;

import com.control.estoquemanagement.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
