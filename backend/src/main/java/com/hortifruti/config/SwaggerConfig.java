package com.hortifruti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 * 
 * @author Hortifruti Team
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura a documentação OpenAPI/Swagger da aplicação.
     * 
     * @return Configuração OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HortiFlow API")
                        .version("1.0.0")
                        .description("API REST para o sistema de gestão HortiFlow - Gerenciamento de produtos, clientes e vendas")
                        .contact(new Contact()
                                .name("HortiFlow Team")
                                .email("admin@hortiflow.co")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}

