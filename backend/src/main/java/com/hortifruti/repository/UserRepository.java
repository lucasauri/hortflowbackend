package com.hortifruti.repository;

import com.hortifruti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório para a entidade User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Busca um usuário pelo email.
     * @param email O email do usuário.
     * @return Um Optional contendo o usuário, se encontrado.
     */
    Optional<User> findByEmail(String email);
}
