package com.hortifruti.controller;

import com.hortifruti.dto.MovimentacaoRequest;
import com.hortifruti.model.Produto;
import com.hortifruti.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        List<Produto> produtos = produtoService.buscarTodos();
        return ResponseEntity.ok(produtos);
    }

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