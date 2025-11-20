package com.hortifruti.repository;

import com.hortifruti.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade ItemVenda.
 */
@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
    /**
     * Busca todos os itens de uma venda pelo ID da venda.
     * @param vendaId O ID da venda
     * @return Lista de itens da venda
     */
    List<ItemVenda> findByVendaId(Long vendaId);
    
    /**
     * Busca todos os itens de uma venda pelo ID da venda, ordenados pelo nome do produto.
     * @param vendaId O ID da venda
     * @return Lista de itens da venda ordenada pelo nome do produto
     */
    @Query("SELECT iv FROM ItemVenda iv WHERE iv.venda.id = :vendaId ORDER BY iv.produto.nome")
    List<ItemVenda> findByVendaIdOrderByProdutoNome(@Param("vendaId") Long vendaId);
    
    /**
     * Calcula a soma dos subtotais de todos os itens de uma venda.
     * @param vendaId O ID da venda
     * @return A soma dos subtotais
     */
    @Query("SELECT SUM(iv.subtotal) FROM ItemVenda iv WHERE iv.venda.id = :vendaId")
    Double sumSubtotalByVendaId(@Param("vendaId") Long vendaId);
}