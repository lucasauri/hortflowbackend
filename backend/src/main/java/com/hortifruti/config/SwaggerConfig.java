package com.hortifruti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 * 
 * <p>Esta classe configura a documentação automática da API REST usando SpringDoc OpenAPI.
 * A documentação estará disponível em: http://localhost:8080/api/swagger-ui/index.html
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura a documentação OpenAPI/Swagger da aplicação.
     * 
     * <p>Define informações gerais da API, como título, versão, descrição e contato.
     * Também configura os servidores disponíveis para a API.
     * 
     * @return Configuração OpenAPI completa
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
                                .email("admin@hortiflow.co"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Servidor de Desenvolvimento")
                ));
    }

    /**
     * Configura grupos de APIs para organização na documentação.
     * 
     * <p>Agrupa todos os endpoints da API em um único grupo para facilitar a navegação.
     * 
     * @return Configuração de grupo de APIs
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("hortiflow-api")
                .pathsToMatch("/**")
                .build();
    }
}

