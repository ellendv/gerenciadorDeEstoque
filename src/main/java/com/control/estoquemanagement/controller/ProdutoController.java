package com.control.estoquemanagement.controller;

import com.control.estoquemanagement.controller.Error.ApiError;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody ProdutoDto produtoDto) {
        return service.cadastrarProduto(produtoDto) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable("id") Long idProduto) {
        try {
            var produtoDto = service.buscarProduto(idProduto);
            return ResponseEntity.ok(produtoDto);
        } catch (RuntimeException e) {
            ApiError apiError = new ApiError("Produto não encontrado", HttpStatus.NOT_FOUND, ZonedDateTime.now(), "/produtos/" + idProduto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }
}
