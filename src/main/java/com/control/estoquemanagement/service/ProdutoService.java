package com.control.estoquemanagement.service;

import com.control.estoquemanagement.model.Produto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public ProdutoDto cadastrarProduto(ProdutoDto produtoDto) {
        var produto = new Produto();

        Produto save = repository.save(produto);
        return produtoDto;
    }
}
