package com.hortifruti.controller;

import com.hortifruti.model.Produto;
import com.hortifruti.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciamento de produtos.
 * Fornece endpoints para operações CRUD de produtos.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * Construtor com injeção de dependência do serviço.
     * 
     * @param produtoService Serviço de produtos
     */
    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Endpoint para buscar todos os produtos.
     * 
     * @return Lista de todos os produtos
     */
    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        try {
            List<Produto> produtos = produtoService.buscarTodos();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para buscar um produto por ID.
     * 
     * @param id ID do produto
     * @return Produto encontrado ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        try {
            return produtoService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para buscar produtos com estoque baixo.
     * 
     * @return Lista de produtos com estoque baixo
     */
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> buscarComEstoqueBaixo() {
        try {
            List<Produto> produtos = produtoService.buscarComEstoqueBaixo();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para criar um novo produto.
     * 
     * @param produto Produto a ser criado
     * @return Produto criado com status 201
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        try {
            Produto produtoCriado = produtoService.criar(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para atualizar um produto existente.
     * 
     * @param id ID do produto
     * @param produto Dados atualizados do produto
     * @return Produto atualizado ou 404 se não encontrado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        try {
            produto.setId(id);
            return produtoService.atualizar(produto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para remover um produto.
     * 
     * @param id ID do produto a ser removido
     * @return Status 204 se removido com sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (produtoService.remover(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para adicionar movimentação de estoque.
     * 
     * @param id ID do produto
     * @param movimentacao Dados da movimentação
     * @return Status 200 se movimentação realizada com sucesso
     */
    @PostMapping("/{id}/movimentacao")
    public ResponseEntity<Map<String, String>> adicionarMovimentacao(
            @PathVariable Long id,
            @RequestBody Map<String, Object> movimentacao) {
        try {
            String tipo = (String) movimentacao.get("tipo");
            Double quantidade = Double.valueOf(movimentacao.get("quantidade").toString());
            
            produtoService.adicionarMovimentacao(id, tipo, quantidade);
            
            return ResponseEntity.ok(Map.of("message", "Movimentação registrada com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro interno do servidor"));
        }
    }

    /**
     * Endpoint para obter estatísticas dos produtos.
     * 
     * @return Estatísticas gerais dos produtos
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            Map<String, Object> estatisticas = produtoService.obterEstatisticas();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint de saúde da API.
     * 
     * @return Status da API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "API de Produtos funcionando corretamente",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
} 