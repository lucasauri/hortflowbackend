package com.hortifruti.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados SQLite.
 * Implementa o padrão Singleton para garantir uma única instância da conexão.
 * 
 * @author Sistema Hortifruti
 * @version 1.0
 * @since 2024-01-01
 */
// LEGADO DESATIVADO: não utilizado após migração para JPA/PostgreSQL
public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:sqlite:hortiflow.db";
    private static DatabaseConnection instance;
    private Connection connection;
    
    /**
     * Construtor privado que inicializa o banco de dados.
     */
    private DatabaseConnection() {
        inicializarBanco();
    }
    
    /**
     * Obtém a instância única da conexão com o banco de dados.
     * 
     * @return Instância única do DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Obtém a conexão com o banco de dados.
     * 
     * @return Conexão com o banco
     * @throws SQLException Se houver erro na conexão
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    /**
     * Inicializa o banco de dados criando as tabelas se não existirem.
     */
    private void inicializarBanco() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Criar tabela de produtos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS produtos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL,
                    embalagem TEXT DEFAULT 'Band. 200m',
                    estoque_inicial REAL DEFAULT 0,
                    entradas REAL DEFAULT 0,
                    saidas REAL DEFAULT 0
                )
            """);
            
            // Criar tabela de movimentações de estoque
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS movimentacoes_estoque (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    produto_id INTEGER,
                    tipo TEXT NOT NULL,
                    quantidade REAL NOT NULL,
                    data DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(produto_id) REFERENCES produtos(id)
                )
            """);
            
            // Criar tabela de clientes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    estado TEXT,
                    telefone TEXT,
                    cnpj TEXT,
                    ie TEXT,
                    cond_pgto TEXT,
                    banco TEXT
                )
            """);
            
            // Criar tabela de pedidos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pedidos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    data TEXT NOT NULL,
                    cliente_id INTEGER,
                    total REAL NOT NULL,
                    FOREIGN KEY(cliente_id) REFERENCES clientes(id)
                )
            """);
            
            // Criar tabela de itens do pedido
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS itens_pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pedido_id INTEGER,
                    produto_id INTEGER,
                    quantidade INTEGER NOT NULL,
                    valor_unitario REAL NOT NULL,
                    FOREIGN KEY(pedido_id) REFERENCES pedidos(id),
                    FOREIGN KEY(produto_id) REFERENCES produtos(id)
                )
            """);
            
            System.out.println("Banco de dados inicializado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Fecha a conexão com o banco de dados.
     */
    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
} 