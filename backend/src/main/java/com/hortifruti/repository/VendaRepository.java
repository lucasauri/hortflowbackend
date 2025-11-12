package com.hortifruti.repository;

import com.hortifruti.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    Optional<Venda> findByNumeroVenda(String numeroVenda);
    
    List<Venda> findByClienteIdOrderByDataVendaDesc(Long clienteId);
    
    List<Venda> findByStatusOrderByDataVendaDesc(Venda.StatusVenda status);
    
    List<Venda> findByDataVendaBetweenOrderByDataVendaDesc(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    @Query("SELECT v FROM Venda v WHERE v.dataVenda >= :dataInicio AND v.dataVenda <= :dataFim AND v.status = :status")
    List<Venda> findByDataVendaBetweenAndStatus(
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim, 
            @Param("status") Venda.StatusVenda status);
    
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.status = 'FINALIZADA'")
    Long countVendasFinalizadas();
    
    @Query("SELECT COALESCE(SUM(v.valorFinal), 0) FROM Venda v WHERE v.status = 'FINALIZADA'")
    Double sumValorVendasFinalizadas();
    
    @Query("SELECT v FROM Venda v ORDER BY v.dataVenda DESC")
    List<Venda> findAllOrderByDataVendaDesc();
}