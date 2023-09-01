package com.control.estoquemanagement.service;

import com.control.estoquemanagement.model.Enum.SituacaoEstoque;
import com.control.estoquemanagement.model.Produto;
import com.control.estoquemanagement.model.dto.ProdutoDetalheDto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Produto produto = buscarProdutoPorId(idProduto);

        return converterParaProdutoDto(produto);
    }

    @Transactional
    public ProdutoDetalheDto buscarProdutosPorNome(String nome) {
        Produto produto = repository.findProdutoByDescricao(nome);

        return converterParaProdutoDetalheDto(produto);
    }

    public List<ProdutoDetalheDto> listarProdutos() {
        List<Produto> produtos = repository.findAll();
        return produtos.stream()
                .map(this::converterParaProdutoDetalheDto)
                .collect(Collectors.toList());
    }

    public ProdutoDto atualizarProduto(Long idProduto, ProdutoDto produtoDto) {
        Produto produtoExistente = buscarProdutoPorId(idProduto);

        atualizarDadosProduto(produtoExistente, produtoDto);

        Produto produtoAtualizado = repository.save(produtoExistente);

        return converterParaProdutoDto(produtoAtualizado);
    }

    public Boolean deletarProduto(Long idProduto) {
        Produto produto = buscarProdutoPorId(idProduto);
        if (produto != null) {
            repository.delete(produto);
            return true;
        }
        return false;
    }

    private Produto buscarProdutoPorId(Long idProduto) {
        return repository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + idProduto));
    }

    private void validarProdutoDto(ProdutoDto produtoDto) {
        if (produtoDto == null || produtoDto.getDescricao().isEmpty() || produtoDto.getValor() <= 0 || produtoDto.getEstoqueAtual() <= 0 || produtoDto.getEstoqueMinimo() <= 0) {
            throw new IllegalArgumentException("ProdutoDto inválido");
        }
    }

    private void atualizarDadosProduto(Produto produto, ProdutoDto produtoDto) {
        produto.setDescricao(produtoDto.getDescricao());
        produto.setValor(produtoDto.getValor());
        produto.setEstoqueMinimo(produtoDto.getEstoqueMinimo());
        produto.setEstoqueAtual(produtoDto.getEstoqueAtual());
    }

    private ProdutoDto converterParaProdutoDto(Produto produto) {
        return new ProdutoDto(
                produto.getDescricao(),
                produto.getValor(),
                produto.getEstoqueMinimo(),
                produto.getEstoqueAtual()
        );
    }

    private ProdutoDetalheDto converterParaProdutoDetalheDto(Produto produto) {
        ProdutoDetalheDto produtoDetalheDto = new ProdutoDetalheDto();
        produtoDetalheDto.setDescricao(produto.getDescricao());
        produtoDetalheDto.setEstoqueAtual(produto.getEstoqueAtual());

        if (produto.getEstoqueAtual() <= 0) {
            produtoDetalheDto.setStatus(String.valueOf(SituacaoEstoque.SemEstoque));
        } else if (produto.getEstoqueAtual() <= produto.getEstoqueMinimo()) {
            produtoDetalheDto.setStatus(String.valueOf(SituacaoEstoque.EsqutoquePerigoso));
        } else {
            produtoDetalheDto.setStatus(String.valueOf(SituacaoEstoque.EstoqueConfortavel));
        }

        return produtoDetalheDto;
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
