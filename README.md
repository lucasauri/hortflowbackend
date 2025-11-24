🍎 HortiFlow - Sistema de Gestão de Hortifrúti
O HortiFlow é uma aplicação API RESTful desenvolvida com Java e Spring Boot 3.3.4 para gerenciar produtos, clientes, estoque e vendas. O sistema utiliza PostgreSQL como banco de dados e implementa funcionalidades avançadas para garantir a integridade e a segurança operacional do negócio.

🏗️ Arquitetura do Projeto: Padrão MVC/Serviço
O design do HortiFlow adota o padrão arquitetural Model-View-Controller (MVC) com uma camada de Serviço dedicada, essencial para isolar a lógica de negócio e manter o código modular.

View (Frontend): A interface gráfica (frontend) envia requisições HTTP para a API.

Controller (C): O pacote controller/ atua como a porta de entrada da API. Ele recebe as requisições e atua como um roteador, delegando o processamento para a camada de Serviço.

Service (Serviço): O pacote service/ é o coração do sistema, onde as regras de negócio são centralizadas (ex: validação de estoque, cálculo de totais). Ele orquestra a comunicação entre o Controller e o Repositório.

Model (M): Os pacotes model/ (estrutura de dados) e repository/ (acesso ao banco) representam a camada Model, que lida com a persistência e a representação dos dados no PostgreSQL.

🎯 Funcionalidades e Inteligência de Negócio
O HortiFlow implementa um conjunto de funcionalidades que garantem a integridade operacional:

Gestão de Estoque e Produtos
Validação Crítica: A principal regra de segurança do sistema impede que qualquer saída seja registrada se o estoque for insuficiente. Essa validação é realizada na camada de Serviço.

Movimentação Detalhada: Permite registrar especificamente ENTRADAS (compras) e SAÍDAS (vendas), mantendo o saldo atualizado.

Visão Gerencial: O Dashboard oferece estatísticas chave, como o valor total do estoque e a lista de produtos em situação de "Estoque Baixo".

Ciclo de Vendas
Transações de Venda: O sistema gerencia o ciclo da venda através dos status PENDENTE, FINALIZADA e CANCELADA.

Devolução Automática: Em caso de cancelamento de uma venda, o serviço retorna automaticamente os produtos para o estoque.

Documentação (PDF): Utilizando a biblioteca iText, o sistema é capaz de gerar o recibo de venda em PDF na finalização da transação.

Segurança e Acesso
Autenticação JWT: A API utiliza o padrão JWT (JSON Web Token) para proteger todos os endpoints de gestão.

Usuário Admin: Um usuário administrador padrão é criado automaticamente na inicialização do sistema para facilitar o primeiro acesso: admin@hortiflow.com / admin123.

Tratamento de Erros: O sistema possui um manipulador global de exceções, garantindo que o backend retorne mensagens de erro claras (como 400 Bad Request ou 500 Internal Server Error) em vez de falhas inesperadas.

⚙️ Como Utilizar o Sistema
Pré-requisitos
Java 17+, Maven 3.6+ e PostgreSQL (com banco hortiflow).

Configuração
Ajuste as credenciais do banco em application.properties:

Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/hortiflow
spring.datasource.username=postgres
spring.datasource.password=123456
Inicialização e Acesso
Backend: Na pasta do projeto, execute: mvn spring-boot:run.

Frontend: Inicie a interface através do script do frontend (ex: frontend/start-frontend.bat).

Acesso: O frontend estará em http://localhost:3000.

Documentação da API: O Swagger UI está disponível em http://localhost:8080/api/swagger-ui/index.html.

Fluxo de Acesso (Login)
Para interagir com as funções de gestão, primeiro obtenha o token de acesso enviando as credenciais do administrador (admin@hortiflow.com / admin123) para o endpoint /auth/login. O token recebido deve ser usado no cabeçalho Authorization: Bearer <token> em todas as requisições subsequentes.

🛣️ Roadmap (Próximos Passos)
O projeto HortiFlow tem planos para expansão futura, incluindo:

Geração de Relatórios Financeiros: Implementar relatórios visuais de vendas por período e lucratividade.

Segurança Avançada: Adicionar níveis de acesso (Roles/Permissões) para diferenciar usuários (Gerente vs. Operador).

Implantação: Empacotar a aplicação em contêineres Docker.

Testes: Criar testes unitários e de integração abrangentes.
