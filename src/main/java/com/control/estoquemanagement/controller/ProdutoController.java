package com.control.estoquemanagement.controller;

import com.control.estoquemanagement.controller.Error.ApiError;
import com.control.estoquemanagement.model.dto.ProdutoDetalheDto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody ProdutoDto produtoDto) {
        return service.cadastrarProduto(produtoDto) ? ResponseEntity.ok(true): ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable("id") Long idProduto) {
        return processarResposta(() -> service.buscarProduto(idProduto), "/produtos/" + idProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarProduto(@PathVariable("id") Long idProduto, @RequestBody ProdutoDto produtoDto) {
        return processarResposta(() -> service.atualizarProduto(idProduto, produtoDto), "/produtos/" + idProduto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluirProduto(@PathVariable("id") Long idProduto) {
        return processarResposta(() -> service.deletarProduto(idProduto), "/produtos/" + idProduto);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProdutoDetalheDto>> listarProdutos() {
        List<ProdutoDetalheDto> produtos = service.listarProdutos();

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/buscar-por-nome/{nome}")
    public ResponseEntity buscarProdutoPorNome(@PathVariable String nome) {
        return processarResposta(() -> service.buscarProdutosPorNome(nome), "/buscar-por-nome/" + nome);
    }

    private ResponseEntity<?> processarResposta(Supplier<?> supplier, String endpoint) {
        try {
            Object resultado = supplier.get();
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            ApiError apiError = new ApiError("Produto não encontrado", HttpStatus.NOT_FOUND, ZonedDateTime.now(), endpoint);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }
}