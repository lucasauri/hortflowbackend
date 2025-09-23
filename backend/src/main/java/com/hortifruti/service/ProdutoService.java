package com.hortifruti.service;
import com.hortifruti.dao.ProdutoDAO;
import com.hortifruti.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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

    private final ProdutoDAO produtoDAO;

    /**
     * Construtor que recebe as dependências via injeção de dependência.
     * 
     * @param produtoDAO DAO para operações de produtos
     */
    @Autowired
    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Busca todos os produtos.
     * 
     * @return Lista de todos os produtos
     * @throws RuntimeException Se houver erro na consulta
     */
    public List<Produto> buscarTodos() {
        try {
            return produtoDAO.buscarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos", e);
        }
    }

    /**
     * Busca um produto por ID.
     * 
     * @param id ID do produto
     * @return Optional contendo o produto, se encontrado
     * @throws RuntimeException Se houver erro na consulta
     */
    public Optional<Produto> buscarPorId(Long id) {
        try {
            return produtoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID: " + id, e);
        }
    }

    /**
     * Busca produtos com estoque baixo.
     * 
     * @return Lista de produtos com estoque baixo
     * @throws RuntimeException Se houver erro na consulta
     */
    public List<Produto> buscarComEstoqueBaixo() {
        try {
            return produtoDAO.buscarComEstoqueBaixo();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos com estoque baixo", e);
        }
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
        
        try {
            return produtoDAO.inserir(produto);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar produto", e);
        }
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
        
        try {
            boolean atualizado = produtoDAO.atualizar(produto);
            if (atualizado) {
                return produtoDAO.buscarPorId(produto.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
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
        
        try {
            return produtoDAO.remover(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto", e);
        }
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
        
        try {
            // Verificar se o produto existe
            Optional<Produto> produtoOpt = produtoDAO.buscarPorId(produtoId);
            if (produtoOpt.isEmpty()) {
                throw new IllegalArgumentException("Produto não encontrado");
            }
            
            // Verificar se há estoque suficiente para saída
            if ("SAIDA".equals(tipo)) {
                Produto produto = produtoOpt.get();
                if (produto.getEstoqueAtual() < quantidade) {
                    throw new IllegalArgumentException("Estoque insuficiente para a saída");
                }
            }
            
            // Atualizar o estoque
            boolean sucesso;
            if ("ENTRADA".equals(tipo)) {
                sucesso = produtoDAO.adicionarEntrada(produtoId, quantidade);
            } else {
                sucesso = produtoDAO.adicionarSaida(produtoId, quantidade);
            }
            
            if (!sucesso) {
                throw new RuntimeException("Falha ao registrar movimentação de estoque");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar movimentação de estoque", e);
        }
    }

    /**
     * Obtém estatísticas gerais dos produtos.
     * 
     * @return Map com as estatísticas
     * @throws RuntimeException Se houver erro na consulta
     */
    public Map<String, Object> obterEstatisticas() {
        try {
            double[] stats = produtoDAO.obterEstatisticas();
            
            Map<String, Object> estatisticas = new HashMap<>();
            estatisticas.put("totalEstoqueInicial", stats[0]);
            estatisticas.put("totalEntradas", stats[1]);
            estatisticas.put("totalSaidas", stats[2]);
            estatisticas.put("totalEstoqueAtual", stats[3]);
            estatisticas.put("totalProdutos", (int) stats[4]);
            
            // Calcular valor total em estoque
            List<Produto> produtos = produtoDAO.buscarTodos();
            double valorTotalEstoque = produtos.stream()
                    .mapToDouble(Produto::getValorEstoque)
                    .sum();
            estatisticas.put("valorTotalEstoque", valorTotalEstoque);
            
            // Contar produtos com estoque baixo
            long produtosComEstoqueBaixo = produtos.stream()
                    .filter(Produto::isEstoqueBaixo)
                    .count();
            estatisticas.put("produtosComEstoqueBaixo", produtosComEstoqueBaixo);
            
            return estatisticas;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter estatísticas", e);
        }
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