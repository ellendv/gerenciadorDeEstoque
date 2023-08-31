package com.control.estoquemanagement.service;

import com.control.estoquemanagement.model.Produto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Boolean cadastrarProduto(ProdutoDto produtoDto) {
        validarProdutoDto(produtoDto);

        Produto produto = converterParaProduto(produtoDto);
        Produto savedProduto = repository.save(produto);

        return savedProduto != null;
    }

    public ProdutoDto buscarProduto(Long idProduto) {
        try {
            var produto = repository.findById(idProduto).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            var produtoDto = new ProdutoDto(produto.getDescricao(), produto.getValor(), produto.getEstoqueMinimo(), produto.getEstoqueAtual());
            return produtoDto;
        } catch (RuntimeException e) {
            throw new RuntimeException("Produto não encontrado");
        }
    }





    private void validarProdutoDto(ProdutoDto produtoDto) {
        if (produtoDto == null || produtoDto.getDescricao().isEmpty() || produtoDto.getValor() <= 0 || produtoDto.getEstoqueAtual() <= 0 || produtoDto.getEstoqueMinimo() <= 0) {
            throw new IllegalArgumentException("ProdutoDto inválido");
        }
    }

    private Produto converterParaProduto(ProdutoDto produtoDto) {
        return new Produto(
                produtoDto.getDescricao(),
                produtoDto.getValor(),
                produtoDto.getEstoqueMinimo(),
                produtoDto.getEstoqueAtual()
        );
    }
}
