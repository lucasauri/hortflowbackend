package com.hortifruti.repository;

import com.hortifruti.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    default List<Cliente> findAllSortedByNome() {
        return findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
