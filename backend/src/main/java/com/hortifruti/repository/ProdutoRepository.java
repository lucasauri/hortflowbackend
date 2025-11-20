package com.hortifruti.repository;

import com.hortifruti.model.Produto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Produto.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca todos os produtos ordenados por nome.
     * @return Lista de produtos ordenada por nome.
     */
    @Query(value = "SELECT * FROM produtos ORDER BY nome", nativeQuery = true)
    List<Produto> findAllOrderByNome();

    /**
     * Busca produtos com estoque baixo.
     * @return Lista de produtos com estoque baixo.
     */
    @Query(value = "SELECT * FROM produtos WHERE (estoque_inicial + entradas - saidas) < 10 ORDER BY nome", nativeQuery = true)
    List<Produto> findComEstoqueBaixo();

    /**
     * Busca todos os produtos ordenados por nome.
     * @return Lista de produtos ordenada por nome.
     */
    default List<Produto> findAllSortedByNome() {
        return findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    /**
     * Incrementa a entrada de um produto.
     * @param id O ID do produto.
     * @param quantidade A quantidade a ser incrementada.
     * @return O número de registros atualizados.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE produtos SET entradas = entradas + :qtd WHERE id = :id", nativeQuery = true)
    int incrementarEntrada(@Param("id") Long id, @Param("qtd") Double quantidade);

    /**
     * Incrementa a saída de um produto.
     * @param id O ID do produto.
     * @param quantidade A quantidade a ser incrementada.
     * @return O número de registros atualizados.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE produtos SET saidas = saidas + :qtd WHERE id = :id", nativeQuery = true)
    int incrementarSaida(@Param("id") Long id, @Param("qtd") Double quantidade);
}
