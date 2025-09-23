package com.hortifruti.dao;

import com.hortifruti.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações de banco de dados relacionadas a Produtos.
 * Implementa o padrão DAO para abstrair a persistência de dados.
 * 
 * @author Sistema Hortifruti
 * @version 1.0
 * @since 2024-01-01
 */
// LEGADO DESATIVADO: não utilizado após migração para JPA/PostgreSQL
public class ProdutoDAO {
    
    private final DatabaseConnection connection;
    
    /**
     * Construtor que recebe a conexão com o banco de dados.
     * 
     * @param connection Conexão com o banco de dados
     */
    public ProdutoDAO(DatabaseConnection connection) {
        this.connection = connection;
    }
    
    /**
     * Busca todos os produtos ordenados por nome.
     * 
     * @return Lista de produtos
     * @throws SQLException Se houver erro na consulta
     */
    public List<Produto> buscarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT *, (estoque_inicial + entradas - saidas) as estoque_atual " +
                    "FROM produtos ORDER BY nome";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(mapearResultSet(rs));
            }
        }
        
        return produtos;
    }
    
    /**
     * Busca um produto pelo ID.
     * 
     * @param id ID do produto
     * @return Optional contendo o produto, se encontrado
     * @throws SQLException Se houver erro na consulta
     */
    public Optional<Produto> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT *, (estoque_inicial + entradas - saidas) as estoque_atual " +
                    "FROM produtos WHERE id = ?";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca produtos com estoque baixo (menos de 10 unidades).
     * 
     * @return Lista de produtos com estoque baixo
     * @throws SQLException Se houver erro na consulta
     */
    public List<Produto> buscarComEstoqueBaixo() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT *, (estoque_inicial + entradas - saidas) as estoque_atual " +
                    "FROM produtos WHERE (estoque_inicial + entradas - saidas) < 10 " +
                    "ORDER BY nome";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(mapearResultSet(rs));
            }
        }
        
        return produtos;
    }
    
    /**
     * Insere um novo produto no banco de dados.
     * 
     * @param produto Produto a ser inserido
     * @return Produto inserido com ID gerado
     * @throws SQLException Se houver erro na inserção
     */
    public Produto inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, embalagem, estoque_inicial) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getEmbalagem());
            stmt.setDouble(4, produto.getEstoqueInicial());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir produto");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falha ao obter ID gerado");
                }
            }
        }
        
        return produto;
    }
    
    /**
     * Atualiza um produto existente.
     * 
     * @param produto Produto a ser atualizado
     * @return true se atualizado com sucesso, false caso contrário
     * @throws SQLException Se houver erro na atualização
     */
    public boolean atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, embalagem = ? WHERE id = ?";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getEmbalagem());
            stmt.setLong(4, produto.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Remove um produto pelo ID.
     * 
     * @param id ID do produto a ser removido
     * @return true se removido com sucesso, false caso contrário
     * @throws SQLException Se houver erro na remoção
     */
    public boolean remover(Long id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Atualiza as entradas de um produto.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a ser adicionada
     * @return true se atualizado com sucesso
     * @throws SQLException Se houver erro na atualização
     */
    public boolean adicionarEntrada(Long produtoId, Double quantidade) throws SQLException {
        String sql = "UPDATE produtos SET entradas = entradas + ? WHERE id = ?";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, quantidade);
            stmt.setLong(2, produtoId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Atualiza as saídas de um produto.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a ser removida
     * @return true se atualizado com sucesso
     * @throws SQLException Se houver erro na atualização
     */
    public boolean adicionarSaida(Long produtoId, Double quantidade) throws SQLException {
        String sql = "UPDATE produtos SET saidas = saidas + ? WHERE id = ?";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, quantidade);
            stmt.setLong(2, produtoId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Obtém estatísticas gerais dos produtos.
     * 
     * @return Array com [total_estoque_inicial, total_entradas, total_saidas, total_estoque_atual, total_produtos]
     * @throws SQLException Se houver erro na consulta
     */
    public double[] obterEstatisticas() throws SQLException {
        String sql = "SELECT " +
                    "SUM(estoque_inicial) as total_estoque_inicial, " +
                    "SUM(entradas) as total_entradas, " +
                    "SUM(saidas) as total_saidas, " +
                    "SUM(estoque_inicial + entradas - saidas) as total_estoque_atual, " +
                    "COUNT(*) as total_produtos " +
                    "FROM produtos";
        
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return new double[] {
                    rs.getDouble("total_estoque_inicial"),
                    rs.getDouble("total_entradas"),
                    rs.getDouble("total_saidas"),
                    rs.getDouble("total_estoque_atual"),
                    rs.getDouble("total_produtos")
                };
            }
        }
        
        return new double[5];
    }
    
    /**
     * Mapeia um ResultSet para um objeto Produto.
     * 
     * @param rs ResultSet com os dados do produto
     * @return Objeto Produto mapeado
     * @throws SQLException Se houver erro ao ler os dados
     */
    private Produto mapearResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setEmbalagem(rs.getString("embalagem"));
        produto.setEstoqueInicial(rs.getDouble("estoque_inicial"));
        produto.setEntradas(rs.getDouble("entradas"));
        produto.setSaidas(rs.getDouble("saidas"));
        return produto;
    }
} 