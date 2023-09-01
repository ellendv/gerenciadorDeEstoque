package com.control.estoquemanagement.service;

import com.control.estoquemanagement.controller.Exception.ProdutoDuplicadoException;
import com.control.estoquemanagement.controller.Exception.ProdutoNaoEncontradoException;
import com.control.estoquemanagement.model.Enum.SituacaoEstoque;
import com.control.estoquemanagement.model.Enum.TipoMovimentacao;
import com.control.estoquemanagement.model.Estoque;
import com.control.estoquemanagement.model.Movimentacao;
import com.control.estoquemanagement.model.Produto;
import com.control.estoquemanagement.model.dto.MovimentacaoDetalheDto;
import com.control.estoquemanagement.model.dto.MovimentacaoDto;
import com.control.estoquemanagement.model.dto.ProdutoDetalheDto;
import com.control.estoquemanagement.model.dto.ProdutoDto;
import com.control.estoquemanagement.repository.MovimentacaoRepository;
import com.control.estoquemanagement.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final EstoqueService estoqueService;

    public ProdutoService(ProdutoRepository repository, MovimentacaoRepository movimentacaoRepository, EstoqueService estoqueService) {
        this.repository = repository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.estoqueService = estoqueService;
    }

    public Boolean cadastrarProduto(ProdutoDto produtoDto) {
        validarProdutoDto(produtoDto);

        Produto produto = converterParaProduto(produtoDto);
        Produto savedProduto = repository.save(produto);

        // Atualize a situação do estoque ao cadastrar o produto
        estoqueService.criarEstoqueParaProduto(savedProduto);

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

        estoqueService.atualizarSituacaoEstoque(produtoAtualizado);

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

    public Boolean registrarMovimentacaoProduto(MovimentacaoDto movimentacaoDto) {
        validarMovimentacaoDto(movimentacaoDto);

        Produto produto = buscarProdutoPorId(movimentacaoDto.getProdutoId());

        Movimentacao movimentacao = criarMovimentacao(movimentacaoDto.getTipo(), movimentacaoDto.getQuantidade(), produto);
        int novaQuantidade;
        if (movimentacaoDto.getTipo() == TipoMovimentacao.ENTRADA) {
            novaQuantidade = produto.getEstoqueAtual() + movimentacaoDto.getQuantidade();
        } else if (movimentacaoDto.getTipo() == TipoMovimentacao.SAIDA) {
            if(produto.getEstoqueAtual() < movimentacaoDto.getQuantidade())
                throw new IllegalArgumentException("A quantidade atual em estoque é insuficiente para a movimentação de SAÍDA.");

            novaQuantidade = produto.getEstoqueAtual() - movimentacaoDto.getQuantidade();

        } else {
            throw new RuntimeException("Tipo de movimentação inválido.");
        }

        produto.setEstoqueAtual(novaQuantidade);
        movimentacaoRepository.save(movimentacao);
        Produto produtoAtualizado = repository.save(produto);
        estoqueService.atualizarSituacaoEstoque(produtoAtualizado);

        return produtoAtualizado != null;
    }


    public List<MovimentacaoDetalheDto> buscarEntradasPorIntervaloDeDatas(Long idProduto, LocalDate dataInicial, LocalDate dataFinal) {
        Produto produto = buscarProdutoPorId(idProduto);

        List<Movimentacao> movimentacoes = movimentacaoRepository.findByProdutoAndTipoAndDataBetween(
                produto, TipoMovimentacao.ENTRADA.getValor(), dataInicial, dataFinal);

        if (movimentacoes.isEmpty()) {
            throw new IllegalArgumentException("Não foram encontradas entradas para este produto no período especificado.");
        }

        List<MovimentacaoDetalheDto> detalhesMovimentacoes = new ArrayList<>();

        for (Movimentacao movimentacao : movimentacoes) {
            MovimentacaoDetalheDto movimentacaoDto = new MovimentacaoDetalheDto();
            movimentacaoDto.setTipoMovimentacao(TipoMovimentacao.ENTRADA.name());
            movimentacaoDto.setQuantidadeMovimentada(movimentacao.getQuantidade());
            movimentacaoDto.setData(movimentacao.getData());
            movimentacaoDto.setProduto(new ProdutoDetalheDto(
                    produto.getId(), produto.getDescricao(), produto.getEstoqueAtual(), estoqueService.buscarEstoque(idProduto).getEstoque()));

            detalhesMovimentacoes.add(movimentacaoDto);
        }

        return detalhesMovimentacoes;
    }


    public List<MovimentacaoDetalheDto> buscarSaidasPorIntervaloDeDatas(Long idProduto, LocalDate dataInicial, LocalDate dataFinal) {
        Produto produto = buscarProdutoPorId(idProduto);

        List<Movimentacao> movimentacoes = movimentacaoRepository.findByProdutoAndTipoAndDataBetween(
                produto, TipoMovimentacao.SAIDA.getValor(), dataInicial, dataFinal);

        if (movimentacoes.isEmpty()) {
            throw new IllegalArgumentException("Não foram encontradas saídas para este produto no período especificado.");
        }

        List<MovimentacaoDetalheDto> detalhesMovimentacoes = new ArrayList<>();

        for (Movimentacao movimentacao : movimentacoes) {
            MovimentacaoDetalheDto movimentacaoDto = new MovimentacaoDetalheDto();
            movimentacaoDto.setTipoMovimentacao(TipoMovimentacao.SAIDA.name());
            movimentacaoDto.setQuantidadeMovimentada(movimentacao.getQuantidade());
            movimentacaoDto.setData(movimentacao.getData());
            movimentacaoDto.setProduto(new ProdutoDetalheDto(
                    produto.getId(), produto.getDescricao(), produto.getEstoqueAtual(), estoqueService.buscarEstoque(idProduto).getEstoque()));

            detalhesMovimentacoes.add(movimentacaoDto);
        }

        return detalhesMovimentacoes;
    }


    public List<MovimentacaoDetalheDto> listarEntradasDeTodosProdutos(LocalDate dataInicial, LocalDate dataFinal) {
        List<Movimentacao> entradas = movimentacaoRepository.findAllByTipoAndDataBetween(TipoMovimentacao.ENTRADA.getValor(), dataInicial, dataFinal);

        if (entradas.isEmpty()) {
            throw new IllegalArgumentException("Não foram encontradas entradas no período especificado.");
        }

        return entradas.stream()
                .map(this::converterParaMovimentacaoDetalheDto)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDetalheDto> listarSaidasDeTodosProdutos(LocalDate dataInicial, LocalDate dataFinal) {
        List<Movimentacao> saidas = movimentacaoRepository.findAllByTipoAndDataBetween(TipoMovimentacao.SAIDA.getValor(), dataInicial, dataFinal);

        if (saidas.isEmpty()) {
            throw new IllegalArgumentException("Não foram encontradas saídas no período especificado.");
        }

        return saidas.stream()
                .map(this::converterParaMovimentacaoDetalheDto)
                .collect(Collectors.toList());
    }


    public Boolean estornarEntradaProduto(Long idProduto, MovimentacaoDto movimentacaoDto) {
        validarMovimentacaoDto(movimentacaoDto);

        Produto produto = buscarProdutoPorId(idProduto);

        if (movimentacaoDto.getTipo() != TipoMovimentacao.ENTRADA) {
            throw new IllegalArgumentException("Apenas a entrada pode ser estornada.");
        }

        if (movimentacaoDto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade a ser estornada deve ser maior que zero.");
        }

        if (produto.getEstoqueAtual() < movimentacaoDto.getQuantidade()) {
            throw new IllegalArgumentException("Não é possível estornar uma quantidade maior do que a atual em estoque.");
        }

        Movimentacao estorno = criarMovimentacao(TipoMovimentacao.SAIDA, movimentacaoDto.getQuantidade(), produto);
        produto.setEstoqueAtual(produto.getEstoqueAtual() - movimentacaoDto.getQuantidade());

        movimentacaoRepository.save(estorno);
        repository.save(produto);
        estoqueService.atualizarSituacaoEstoque(produto);

        return true;
    }


    public Boolean estornarSaidaProduto(Long idProduto, MovimentacaoDto movimentacaoDto) {
        validarMovimentacaoDto(movimentacaoDto);

        Produto produto = buscarProdutoPorId(idProduto);

        if (movimentacaoDto.getTipo() != TipoMovimentacao.SAIDA) {
            throw new IllegalArgumentException("Apenas a saída pode ser estornada.");
        }

        if (movimentacaoDto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade a ser estornada deve ser maior que zero.");
        }

        Movimentacao estorno = criarMovimentacao(TipoMovimentacao.ENTRADA, movimentacaoDto.getQuantidade(), produto);
        produto.setEstoqueAtual(produto.getEstoqueAtual() + movimentacaoDto.getQuantidade());

        movimentacaoRepository.save(estorno);
        repository.save(produto);
        estoqueService.atualizarSituacaoEstoque(produto);

        return true;
    }

    public Map<String, Integer> comparativoEntradasSaidas(LocalDate data, String intervalo) {
        LocalDate dataInicial, dataFinal;
        switch (intervalo.toLowerCase()) {
            case "dia":
                dataInicial = data.atStartOfDay().toLocalDate();
                dataFinal = dataInicial.plusDays(1);
                break;
            case "mes":
                dataInicial = data.withDayOfMonth(1);
                dataFinal = data.plusMonths(1).withDayOfMonth(1);
                break;
            case "ano":
                dataInicial = data.withDayOfYear(1);
                dataFinal = data.plusYears(1).withDayOfYear(1);
                break;
            default:
                throw new IllegalArgumentException("Intervalo inválido. Use 'dia', 'mes' ou 'ano'.");
        }

        List<Movimentacao> entradas = movimentacaoRepository.findByTipoAndDataBetween(
                TipoMovimentacao.ENTRADA.getValor(), dataInicial, dataFinal);

        List<Movimentacao> saidas = movimentacaoRepository.findByTipoAndDataBetween(
                TipoMovimentacao.SAIDA.getValor(), dataInicial, dataFinal);

        int totalEntradas = entradas.stream().mapToInt(Movimentacao::getQuantidade).sum();
        int totalSaidas = saidas.stream().mapToInt(Movimentacao::getQuantidade).sum();

        Map<String, Integer> comparativo = new HashMap<>();
        comparativo.put("totalEntradas", totalEntradas);
        comparativo.put("totalSaidas", totalSaidas);

        return comparativo;
    }

    private Movimentacao criarMovimentacao(TipoMovimentacao tipoMovimentacao, int quantidade, Produto produto) {
        if (tipoMovimentacao == TipoMovimentacao.ENTRADA) {
            return new Movimentacao(TipoMovimentacao.ENTRADA.getValor(), LocalDate.now(), quantidade, produto);
        } else if (tipoMovimentacao == TipoMovimentacao.SAIDA) {
            return new Movimentacao(TipoMovimentacao.SAIDA.getValor(), LocalDate.now(), quantidade, produto);
        } else {
            throw new IllegalArgumentException("Tipo de movimentação inválido");
        }
    }




    private void validarMovimentacaoDto(MovimentacaoDto movimentacaoDto) {
        if (movimentacaoDto == null || movimentacaoDto.getProdutoId() == null || movimentacaoDto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("MovimentacaoDto inválido");
        }
    }

    private Produto buscarProdutoPorId(Long idProduto) {
        return repository.findById(idProduto)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com o ID: " + idProduto));
    }

    private void validarProdutoDto(ProdutoDto produtoDto) {
        if (produtoDto == null || produtoDto.getDescricao().isEmpty() || produtoDto.getValor() <= 0 || produtoDto.getEstoqueAtual() <= 0 || produtoDto.getEstoqueMinimo() <= 0) {
            throw new IllegalArgumentException("ProdutoDto inválido");
        }else if (repository.findProdutoByDescricao(produtoDto.getDescricao()) != null) {
            throw new ProdutoDuplicadoException("Já existe um produto cadastrado com essa descrição!");
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
        produtoDetalheDto.setIdProduto(produto.getId());
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


    private MovimentacaoDetalheDto converterParaMovimentacaoDetalheDto(Movimentacao movimentacao) {
        String tipo = movimentacao.getTipo() == TipoMovimentacao.SAIDA.getValor()? "SAIDA":"ENTRADA";

        MovimentacaoDetalheDto movimentacaoDetalheDto = new MovimentacaoDetalheDto();
        movimentacaoDetalheDto.setTipoMovimentacao(tipo);
        movimentacaoDetalheDto.setQuantidadeMovimentada(movimentacao.getQuantidade());
        movimentacaoDetalheDto.setData(movimentacao.getData());
        movimentacaoDetalheDto.setProduto(new ProdutoDetalheDto(
                movimentacao.getProduto().getId(),
                movimentacao.getProduto().getDescricao(),
                movimentacao.getProduto().getEstoqueAtual(),
                estoqueService.buscarEstoque(movimentacao.getProduto().getId()).getEstoque()));

        return movimentacaoDetalheDto;
    }
}
