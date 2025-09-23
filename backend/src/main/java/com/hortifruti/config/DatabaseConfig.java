package com.hortifruti.config;

import com.hortifruti.dao.DatabaseConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para beans relacionados ao banco de dados.
 * 
 * @author Sistema Hortifruti
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class DatabaseConfig {
    
    /**
     * Bean para a conexão com o banco de dados.
     * 
     * @return Instância única do DatabaseConnection
     */
    @Bean
    public DatabaseConnection databaseConnection() {
        return DatabaseConnection.getInstance();
    }
} 