package com.hortifruti.controller;

import com.hortifruti.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller REST para dados do dashboard.
 */
@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final ProdutoService produtoService;

    /**
     * Construtor para injeção de dependências.
     * @param produtoService Serviço de produtos
     */
    public DashboardController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Retorna estatísticas gerais para o dashboard.
     * @return Um mapa com as estatísticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        Map<String, Object> stats = produtoService.obterEstatisticas();
        Map<String, Object> response = new HashMap<>();
        response.put("totalProdutos", stats.getOrDefault("totalProdutos", 0));
        response.put("estoqueAtual", stats.getOrDefault("totalEstoqueAtual", 0));
        response.put("valorEstoque", stats.getOrDefault("valorTotalEstoque", 0));
        response.put("produtosBaixoEstoque", stats.getOrDefault("produtosComEstoqueBaixo", 0));
        return ResponseEntity.ok(response);
    }
}
