package com.hortifruti.service;
import com.hortifruti.model.Produto;
import com.hortifruti.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Camada de serviço para operações de negócio relacionadas a Produtos.
 * Implementa a lógica de negócio e orquestra as operações de dados.
 * 
 * @author Sistema Hortifruti
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    /**
     * Construtor que recebe as dependências via injeção de dependência.
     * 
     * @param produtoRepository Repository para operações de produtos
     */
    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Busca todos os produtos.
     * 
     * @return Lista de todos os produtos
     * @throws RuntimeException Se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<Produto> buscarTodos() {
        // Preferindo ordenação via JPA para manter consistência
        return produtoRepository.findAllSortedByNome();
    }

    /**
     * Busca um produto por ID.
     * 
     * @param id ID do produto
     * @return Optional contendo o produto, se encontrado
     * @throws RuntimeException Se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /**
     * Busca produtos com estoque baixo.
     * 
     * @return Lista de produtos com estoque baixo
     * @throws RuntimeException Se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<Produto> buscarComEstoqueBaixo() {
        return produtoRepository.findComEstoqueBaixo();
    }

    /**
     * Cria um novo produto.
     * 
     * @param produto Produto a ser criado
     * @return Produto criado com ID gerado
     * @throws IllegalArgumentException Se os dados do produto forem inválidos
     * @throws RuntimeException Se houver erro na criação
     */
    public Produto criar(Produto produto) {
        validarProduto(produto);
        // Zera campos de controle, se vierem nulos
        if (produto.getEstoqueInicial() == null) produto.setEstoqueInicial(0.0);
        if (produto.getEntradas() == null) produto.setEntradas(0.0);
        if (produto.getSaidas() == null) produto.setSaidas(0.0);
        return produtoRepository.save(produto);
    }

    /**
     * Atualiza um produto existente.
     * 
     * @param produto Produto a ser atualizado
     * @return Optional contendo o produto atualizado, se encontrado
     * @throws IllegalArgumentException Se os dados do produto forem inválidos
     * @throws RuntimeException Se houver erro na atualização
     */
    public Optional<Produto> atualizar(Produto produto) {
        validarProduto(produto);

        if (produto.getId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para atualização");
        }

        if (!produtoRepository.existsById(produto.getId())) {
            return Optional.empty();
        }
        Produto atualizado = produtoRepository.save(produto);
        return Optional.of(atualizado);
    }

    /**
     * Remove um produto pelo ID.
     * 
     * @param id ID do produto a ser removido
     * @return true se removido com sucesso, false caso contrário
     * @throws RuntimeException Se houver erro na remoção
     */
    public boolean remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para remoção");
        }
        if (!produtoRepository.existsById(id)) return false;
        produtoRepository.deleteById(id);
        return true;
    }

    /**
     * Adiciona uma movimentação de estoque para um produto.
     * 
     * @param produtoId ID do produto
     * @param tipo Tipo da movimentação (ENTRADA, SAIDA)
     * @param quantidade Quantidade movimentada
     * @throws IllegalArgumentException Se os dados forem inválidos
     * @throws RuntimeException Se houver erro na movimentação
     */
    public void adicionarMovimentacao(Long produtoId, String tipo, Double quantidade) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }

        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo da movimentação é obrigatório");
        }

        if (!"ENTRADA".equals(tipo) && !"SAIDA".equals(tipo)) {
            throw new IllegalArgumentException("Tipo deve ser ENTRADA ou SAIDA");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        // Verificar se o produto existe
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Verificar se há estoque suficiente para saída
        if ("SAIDA".equals(tipo) && produto.getEstoqueAtual() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para a saída");
        }

        // Atualizar o estoque via queries nativas para evitar condições de corrida simples
        int rows;
        if ("ENTRADA".equals(tipo)) {
            rows = produtoRepository.incrementarEntrada(produtoId, quantidade);
        } else {
            rows = produtoRepository.incrementarSaida(produtoId, quantidade);
        }
        if (rows == 0) {
            throw new RuntimeException("Falha ao registrar movimentação de estoque");
        }
    }

    /**
     * Obtém estatísticas gerais dos produtos.
     * 
     * @return Map com as estatísticas
     * @throws RuntimeException Se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obterEstatisticas() {
        List<Produto> produtos = produtoRepository.findAll();

        double totalEstoqueInicial = produtos.stream().map(Produto::getEstoqueInicial).filter(v -> v != null).mapToDouble(Double::doubleValue).sum();
        double totalEntradas = produtos.stream().map(Produto::getEntradas).filter(v -> v != null).mapToDouble(Double::doubleValue).sum();
        double totalSaidas = produtos.stream().map(Produto::getSaidas).filter(v -> v != null).mapToDouble(Double::doubleValue).sum();
        double totalEstoqueAtual = produtos.stream().mapToDouble(p -> p.getEstoqueAtual()).sum();
        int totalProdutos = produtos.size();

        double valorTotalEstoque = produtos.stream()
                .mapToDouble(Produto::getValorEstoque)
                .sum();
        long produtosComEstoqueBaixo = produtos.stream()
                .filter(Produto::isEstoqueBaixo)
                .count();

        Map<String, Object> estatisticas = new HashMap<>();
        estatisticas.put("totalEstoqueInicial", totalEstoqueInicial);
        estatisticas.put("totalEntradas", totalEntradas);
        estatisticas.put("totalSaidas", totalSaidas);
        estatisticas.put("totalEstoqueAtual", totalEstoqueAtual);
        estatisticas.put("totalProdutos", totalProdutos);
        estatisticas.put("valorTotalEstoque", valorTotalEstoque);
        estatisticas.put("produtosComEstoqueBaixo", produtosComEstoqueBaixo);
        return estatisticas;
    }

    /**
     * Valida os dados de um produto.
     * 
     * @param produto Produto a ser validado
     * @throws IllegalArgumentException Se os dados forem inválidos
     */
    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        
        if (produto.getEstoqueInicial() != null && produto.getEstoqueInicial() < 0) {
            throw new IllegalArgumentException("Estoque inicial não pode ser negativo");
        }
    }
} 