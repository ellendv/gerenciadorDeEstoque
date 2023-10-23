package com.control.estoquemanagement.service;

import com.control.estoquemanagement.model.Enum.SituacaoEstoque;
import com.control.estoquemanagement.model.Enum.TipoMovimentacao;
import com.control.estoquemanagement.model.Estoque;
import com.control.estoquemanagement.model.Produto;
import com.control.estoquemanagement.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public void criarEstoqueParaProduto(Produto produto) {
        String situacaoEstoque = calcularSituacaoEstoque(produto.getEstoqueAtual(), produto.getEstoqueMinimo());
        Estoque estoque = new Estoque(produto.getId(),situacaoEstoque);
        estoqueRepository.save(estoque);
    }

    public void atualizarSituacaoEstoque(Produto produto) {
        try {
            Estoque estoque = estoqueRepository.findByIdProduto(produto.getId());

            if (estoque != null) {
                estoque.setEstoque(calcularSituacaoEstoque(produto.getEstoqueAtual(), produto.getEstoqueMinimo()));
                estoqueRepository.save(estoque);
            } else
                throw new IllegalArgumentException("Produto sem estoque relacionado!");

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar a situação do estoque: " + e.getMessage(), e);
        }
    }

    public Estoque buscarEstoque(Long idProduto){
        return estoqueRepository.findByIdProduto(idProduto);
    }

    private String calcularSituacaoEstoque(int estoqueAtual, int estoqueMinimo) {
        if (estoqueAtual <= 0) {
            return SituacaoEstoque.SemEstoque.name();
        } else if (estoqueAtual <= estoqueMinimo) {
            return SituacaoEstoque.EsqutoquePerigoso.name();
        } else {
            return SituacaoEstoque.EstoqueConfortavel.name();
        }
    }
}





