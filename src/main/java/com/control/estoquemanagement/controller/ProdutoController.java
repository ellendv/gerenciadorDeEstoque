package com.control.estoquemanagement.controller;

import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping("/produtos")
    public ResponseEntity cadastrarProduto(@Valid @RequestBody ProdutoDto produtoDto){

        return service.cadastrarProduto(produtoDto) == null ? ResponseEntity.badRequest().body(null) : ResponseEntity.ok().body(produtoDto);

    }
}
