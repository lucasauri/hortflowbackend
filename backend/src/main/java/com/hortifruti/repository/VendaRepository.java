package com.hortifruti.repository;

import com.hortifruti.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Venda.
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    /**
     * Busca uma venda pelo número da venda.
     * @param numeroVenda O número da venda.
     * @return Um Optional contendo a venda, se encontrada.
     */
    Optional<Venda> findByNumeroVenda(String numeroVenda);

    /**
     * Busca vendas de um cliente ordenadas pela data da venda em ordem decrescente.
     * @param clienteId O ID do cliente.
     * @return Uma lista de vendas do cliente.
     */
    List<Venda> findByClienteIdOrderByDataVendaDesc(Long clienteId);

    /**
     * Busca vendas por status ordenadas pela data da venda em ordem decrescente.
     * @param status O status da venda.
     * @return Uma lista de vendas com o status especificado.
     */
    List<Venda> findByStatusOrderByDataVendaDesc(Venda.StatusVenda status);

    /**
     * Busca vendas em um período de tempo ordenadas pela data da venda em ordem decrescente.
     * @param dataInicio A data de início do período.
     * @param dataFim A data de fim do período.
     * @return Uma lista de vendas no período especificado.
     */
    List<Venda> findByDataVendaBetweenOrderByDataVendaDesc(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * Busca vendas em um período de tempo com um status específico.
     * @param dataInicio A data de início do período.
     * @param dataFim A data de fim do período.
     * @param status O status da venda.
     * @return Uma lista de vendas que correspondem aos critérios.
     */
    @Query("SELECT v FROM Venda v WHERE v.dataVenda >= :dataInicio AND v.dataVenda <= :dataFim AND v.status = :status")
    List<Venda> findByDataVendaBetweenAndStatus(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("status") Venda.StatusVenda status);

    /**
     * Conta o número de vendas finalizadas.
     * @return O número de vendas finalizadas.
     */
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.status = 'FINALIZADA'")
    Long countVendasFinalizadas();

    /**
     * Soma o valor total das vendas finalizadas.
     * @return O valor total das vendas finalizadas.
     */
    @Query("SELECT COALESCE(SUM(v.valorFinal), 0) FROM Venda v WHERE v.status = 'FINALIZADA'")
    Double sumValorVendasFinalizadas();

    /**
     * Busca todas as vendas ordenadas pela data da venda em ordem decrescente.
     * @return Uma lista de todas as vendas.
     */
    @Query("SELECT v FROM Venda v ORDER BY v.dataVenda DESC")
    List<Venda> findAllOrderByDataVendaDesc();

    /**
     * Busca uma venda com seus itens, produto e cliente carregados (evita LazyInitializationException para PDF).
     * @param id O ID da venda.
     * @return Um Optional contendo a venda com as informações carregadas, se encontrada.
     */
    @Query("SELECT v FROM Venda v \n"
            + "LEFT JOIN FETCH v.itens i \n"
            + "LEFT JOIN FETCH i.produto p \n"
            + "LEFT JOIN FETCH v.cliente c \n"
            + "WHERE v.id = :id")
    Optional<Venda> findByIdWithItensProdutoCliente(@Param("id") Long id);
}