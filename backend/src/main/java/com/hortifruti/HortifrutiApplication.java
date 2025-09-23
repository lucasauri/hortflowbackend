package com.hortifruti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Hortifruti.
 * Inicializa o servidor Spring Boot com todas as configurações necessárias.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class HortifrutiApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot.
     * 
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema HortiFlow...");
        SpringApplication.run(HortifrutiApplication.class, args);
        System.out.println("✅ Sistema HortiFlow iniciado com sucesso!");
        System.out.println("📊 API base: http://localhost:8080/api");
        System.out.println("🔍 Swagger UI: http://localhost:8080/api/swagger-ui/index.html");
    }
} 