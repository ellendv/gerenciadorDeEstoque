package com.control.estoquemanagement.controller;

import com.control.estoquemanagement.controller.Exception.ApiError;
import com.control.estoquemanagement.controller.Exception.ProdutoDuplicadoException;
import com.control.estoquemanagement.controller.Exception.ProdutoNaoEncontradoException;
import com.control.estoquemanagement.model.Movimentacao;
import com.control.estoquemanagement.model.dto.MovimentacaoDetalheDto;
import com.control.estoquemanagement.model.dto.MovimentacaoDto;
import com.control.estoquemanagement.model.dto.ProdutoDetalheDto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.service.ProdutoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return processarResposta(()->service.cadastrarProduto(produtoDto),"/produto");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable("id") Long idProduto) {
        return processarResposta(() -> service.buscarProduto(idProduto), "/produto/" + idProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarProduto(@PathVariable("id") Long idProduto, @RequestBody ProdutoDto produtoDto) {
        return processarResposta(() -> service.atualizarProduto(idProduto, produtoDto), "/produto/" + idProduto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluirProduto(@PathVariable("id") Long idProduto) {
        return processarResposta(() -> service.deletarProduto(idProduto), "/produto/" + idProduto);
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

    @PostMapping("/movimentacao")
    public ResponseEntity<?> registrarMovimentacaoProduto(@RequestBody MovimentacaoDto movimentacaoDto) {
        return processarResposta(() -> service.registrarMovimentacaoProduto(movimentacaoDto), "/registrar-entrada");
    }

    @GetMapping("/{id}/entradas")
    public ResponseEntity buscarEntradasPorIntervaloDeDatas(
            @PathVariable("id") Long idProduto,
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        return processarResposta(() -> service.buscarEntradasPorIntervaloDeDatas(idProduto, dataInicial, dataFinal), "/produto/" + idProduto + "/entradas");
    }


    @GetMapping("/{id}/saidas")
    public ResponseEntity buscarSaidasPorProduto(
            @PathVariable("id") Long idProduto,
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        return processarResposta(() -> service.buscarSaidasPorIntervaloDeDatas(idProduto, dataInicial, dataFinal), "/produto/" + idProduto + "/saidas");
    }

    @GetMapping("/entradas")
    public ResponseEntity<?> listarEntradasDeTodosProdutos(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        return processarResposta(() -> service.listarEntradasDeTodosProdutos(dataInicial, dataFinal), "/entradas");
    }


    @GetMapping("/saidas")
    public ResponseEntity<?> listarSaidasDeTodosProdutos(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        return processarResposta(() -> service.listarSaidasDeTodosProdutos(dataInicial, dataFinal), "/saidas");
    }

    @PutMapping("/{id}/estornar-entrada")
    public ResponseEntity<?> estornarEntradaProduto(@PathVariable("id") Long idProduto, @RequestBody MovimentacaoDto movimentacaoDto) {
        return processarResposta(() -> service.estornarEntradaProduto(idProduto, movimentacaoDto), "/produto/" + idProduto + "/estornar-entrada");
    }

    @PutMapping("/{id}/estornar-saida")
    public ResponseEntity<?> estornarSaidaProduto(@PathVariable("id") Long idProduto, @RequestBody MovimentacaoDto movimentacaoDto) {
        return processarResposta(() -> service.estornarSaidaProduto(idProduto, movimentacaoDto), "/produto/" + idProduto + "/estornar-saida");
    }

    @GetMapping("/comparativo")
    public ResponseEntity<?> comparativoEntradasSaidas(
            @RequestParam(name = "data", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(name = "intervalo", required = true) String intervalo) {
        return processarResposta(() -> service.comparativoEntradasSaidas(data, intervalo), "/comparativo");
    }


    private ResponseEntity<?> processarResposta(Supplier<?> supplier, String endpoint) {
        try {
            Object resultado = supplier.get();
            return ResponseEntity.ok(resultado);
        } catch (ProdutoNaoEncontradoException ex) {
            ApiError apiError = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(), endpoint);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (ProdutoDuplicadoException ex) {
            ApiError apiError = new ApiError(ex.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(), endpoint);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        } catch (IllegalArgumentException ex) {
            ApiError apiError = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(), endpoint);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        } catch (RuntimeException ex) {
            ApiError apiError = new ApiError("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now(), endpoint);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }
}