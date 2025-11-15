package com.hortifruti.controller;

import com.hortifruti.dto.MovimentacaoRequest;
import com.hortifruti.model.Produto;
import com.hortifruti.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciamento de produtos.
 * 
 * <p>Fornece endpoints para CRUD de produtos, consulta de estoque baixo,
 * movimentações de estoque e verificação de saúde do serviço.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos e estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Lista todos os produtos cadastrados.
     * 
     * @return Lista de produtos ordenada por nome
     */
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista de todos os produtos cadastrados, ordenados por nome")
    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        List<Produto> produtos = produtoService.buscarTodos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca um produto por ID.
     * 
     * @param id ID do produto
     * @return Produto encontrado ou 404 se não existir
     */
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu identificador")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> buscarComEstoqueBaixo() {
        List<Produto> produtos = produtoService.buscarComEstoqueBaixo();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK"));
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        Produto produtoCriado = produtoService.criar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        produto.setId(id);
        return produtoService.atualizar(produto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (produtoService.remover(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/movimentacao")
    public ResponseEntity<Map<String, String>> adicionarMovimentacao(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoRequest movimentacao) {
        produtoService.adicionarMovimentacao(id, movimentacao.getTipo(), movimentacao.getQuantidade());
        return ResponseEntity.ok(Map.of("message", "Movimentação registrada com sucesso"));
    }
}