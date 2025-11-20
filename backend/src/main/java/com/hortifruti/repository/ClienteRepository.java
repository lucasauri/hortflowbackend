package com.hortifruti.repository;

import com.hortifruti.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Cliente.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    /**
     * Retorna todos os clientes ordenados por nome em ordem ascendente.
     * @return Lista de clientes ordenada por nome
     */
    default List<Cliente> findAllSortedByNome() {
        return findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
