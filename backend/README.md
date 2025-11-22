# HortiFlow Backend API

Sistema de gerenciamento para HortiFlow — aplicação de gestão de vendas e estoque para uma frutaria. Backend desenvolvido com **Spring Boot 3.3.4** e **Java 17**.

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Setup](#instalação-e-setup)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração](#configuração)
- [Rodando a Aplicação](#rodando-a-aplicação)
- [Endpoints da API](#endpoints-da-api)
- [Autenticação](#autenticação)
- [Banco de Dados](#banco-de-dados)
- [Ferramentas e Dependências](#ferramentas-e-dependências)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

HortiFlow Backend é uma API REST que gerencia:
- **Autenticação**: Login, refresh de tokens, logout com JWT
- **Clientes**: CRUD completo com endereços
- **Produtos**: Gerenciamento de estoque, movimentações
- **Vendas**: Criação, finalização, cancelamento com PDF de recibo
- **Relatórios**: Vendas por período, cliente, produto
- **Dashboard**: Estatísticas gerais (estoque, produtos, valores)

**Características principais:**
- ✅ Segurança com Spring Security + JWT
- ✅ Documentação automática com Swagger/OpenAPI
- ✅ Geração de PDF com iText
- ✅ Validação de dados com annotations
- ✅ CORS habilitado para frontend
- ✅ Tratamento centralizado de erros
- ✅ Logging estruturado

---

## 📦 Pré-requisitos

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 12+** ([Download](https://www.postgresql.org/download/))
- **Git** (opcional, para controle de versão)

### Verificar instalação

```powershell
java -version
mvn -version
psql --version
```

---

## 🚀 Instalação e Setup

### 1. Clonar o repositório (ou extrair arquivo)

```powershell
cd C:\dsv
# Se usando git:
git clone <url-do-repositorio>
# Caso contrário, extrair o arquivo ZIP fornecido
```

### 2. Criar banco de dados PostgreSQL

```powershell
# Conectar ao PostgreSQL
psql -U postgres

# No prompt psql:
CREATE DATABASE hortiflow;
CREATE USER hortiflow_user WITH PASSWORD 'seu_password_seguro';
GRANT ALL PRIVILEGES ON DATABASE hortiflow TO hortiflow_user;
\q
```

Ou usar uma ferramenta GUI como **pgAdmin** ou **DBeaver**.

### 3. Instalar dependências Maven

```powershell
cd C:\dsv\ProjetoHortiflow\backend
mvn clean install
```

Isso irá:
- Baixar todas as dependências
- Compilar o código
- Executar testes (se existirem)
- Gerar o JAR da aplicação

---

## 📁 Estrutura do Projeto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/hortifruti/
│   │   │   ├── HortifrutiApplication.java       # Ponto de entrada (main)
│   │   │   ├── config/
│   │   │   │   ├── AdminSeed.java               # Cria usuário admin na primeira inicialização
│   │   │   │   └── SecurityConfig.java          # Configuração de segurança e CORS
│   │   │   ├── controller/                      # Controladores REST (endpoints HTTP)
│   │   │   │   ├── AuthController.java          # Login, refresh, logout
│   │   │   │   ├── ClienteController.java       # CRUD de clientes
│   │   │   │   ├── ProdutoController.java       # CRUD de produtos e movimentações
│   │   │   │   ├── VendaController.java         # Gerenciamento de vendas e PDF
│   │   │   │   ├── RelatorioController.java     # Geração de relatórios
│   │   │   │   └── DashboardController.java     # Estatísticas do dashboard
│   │   │   ├── service/                         # Lógica de negócio
│   │   │   │   ├── AuthService.java             # Autenticação e tokenização JWT
│   │   │   │   ├── ClienteService.java          # Regras de negócio de clientes
│   │   │   │   ├── ProdutoService.java          # Gerenciamento de produtos e estoque
│   │   │   │   ├── VendaService.java            # Processamento de vendas
│   │   │   │   ├── RelatorioService.java        # Geração de relatórios
│   │   │   │   └── PdfService.java              # Geração de PDFs
│   │   │   ├── repository/                      # Acesso a dados (Spring Data JPA)
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   ├── VendaRepository.java
│   │   │   │   └── VendaItemRepository.java
│   │   │   ├── model/                           # Entidades JPA (mapeamento com BD)
│   │   │   │   ├── User.java                    # Usuário/Admin
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Endereco.java
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Venda.java
│   │   │   │   └── VendaItem.java
│   │   │   ├── dto/                             # Data Transfer Objects (entrada/saída)
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── UserDto.java
│   │   │   │   ├── ClienteEnderecoRequest.java
│   │   │   │   ├── ClienteEnderecoResponse.java
│   │   │   │   ├── MovimentacaoRequest.java
│   │   │   │   └── RelatorioVendasDTO.java
│   │   │   ├── security/                        # Utilitários de segurança
│   │   │   │   ├── JwtAuthenticationFilter.java # Filtro para validar JWT
│   │   │   │   └── JwtTokenProvider.java        # Geração e validação de tokens
│   │   │   ├── exception/                       # Exceções customizadas
│   │   │   │   ├── ApiExceptionHandler.java     # Handler global de exceções
│   │   │   │   └── [CustomExceptions].java
│   │   │   └── dao/                             # DAOs customizados (se necessário)
│   │   └── resources/
│   │       ├── application.properties           # Configurações da aplicação
│   │       └── db/migration/                    # Scripts SQL (Flyway/Liquibase, se usado)
│   └── test/                                    # Testes unitários e integração
│
├── pom.xml                                      # Dependências Maven
├── README.md                                    # Este arquivo
└── target/                                      # Artefatos compilados (JAR, classes)
```

### Explicação das camadas:

| Camada | Responsabilidade |
|--------|------------------|
| **Controller** | Receber requisições HTTP, validar entrada, retornar respostas |
| **Service** | Implementar lógica de negócio, orquestrar chamadas |
| **Repository** | Abstrair operações de persistência no banco |
| **Model** | Entidades JPA que representam tabelas do BD |
| **DTO** | Estruturas de entrada/saída que protegem entidades |
| **Security** | Filtros, provedores e utilitários de autenticação |
| **Config** | Configurações globais (Swagger, CORS, seeds) |

---

## ⚙️ Configuração

### `application.properties`

Arquivo localizado em `src/main/resources/application.properties`. Principais configurações:

```properties
# Servidor
server.port=8080
server.servlet.context-path=/api

# Banco de Dados PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/hortiflow
spring.datasource.username=postgres
spring.datasource.password=123456

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# JWT
app.jwt.secret=change-me-please-32bytes-minimum-secret-key-123456
app.jwt.access.exp=900              # Token válido por 15 minutos (em segundos)
app.jwt.refresh.exp=604800          # Refresh token válido por 7 dias

# Swagger/OpenAPI
springdoc.swagger-ui.path=/swagger-ui
springdoc.api-docs.path=/v3/api-docs
```

**Importante:** Em produção, altere:
- `app.jwt.secret` para uma chave segura (mínimo 32 caracteres)
- Credenciais do banco para um usuário com permissões mínimas
- `spring.jpa.hibernate.ddl-auto` para `validate` (não criar schemas automaticamente)

---

## ▶️ Rodando a Aplicação

### Opção 1: Usando Maven (recomendado para desenvolvimento)

```powershell
cd C:\dsv\ProjetoHortiflow\backend

# Compilar e rodar
mvn spring-boot:run
```

A aplicação iniciará em `http://localhost:8080/api`.

### Opção 2: Compilar e rodar o JAR

```powershell
cd C:\dsv\ProjetoHortiflow\backend

# Gerar o JAR (compilação completa)
mvn clean package -DskipTests

# Rodar o JAR
java -jar target/hortifruti-backend-1.0.0.jar
```

### Verificar se está rodando

- **API Health**: `http://localhost:8080/api/produtos/health`
- **Swagger UI**: `http://localhost:8080/api/swagger-ui/index.html`
- **API Docs JSON**: `http://localhost:8080/api/v3/api-docs`

---

## 📡 Endpoints da API

Todos os endpoints estão prefixados com `/api`. O banco de dados será inicializado com um usuário admin padrão:
- **Email**: `admin@hortiflow.com`
- **Senha**: `admin123`

### 🔐 Autenticação (`/auth`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/auth/login` | Realiza login | ❌ |
| POST | `/auth/refresh` | Renova access token | ❌ |
| GET | `/auth/me` | Perfil do usuário logado | ✅ |
| POST | `/auth/logout` | Faz logout | ✅ |

**Exemplo de Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@hortiflow.com",
    "password": "admin123"
  }'
```

**Resposta sucesso:**
```json
{
  "success": true,
  "message": "Login efetuado",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "...",
    "email": "admin@hortiflow.com",
    "role": "ADMIN"
  }
}
```

### 👥 Clientes (`/clientes`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/clientes` | Lista todos os clientes | ✅ |
| GET | `/clientes/{id}` | Busca cliente por ID | ✅ |
| POST | `/clientes` | Cria novo cliente | ✅ |
| POST | `/clientes/com-endereco` | Cria cliente com endereço | ✅ |
| PUT | `/clientes/{id}` | Atualiza cliente | ✅ |
| DELETE | `/clientes/{id}` | Remove cliente | ✅ |

**Exemplo de criar cliente com endereço:**

```bash
curl -X POST http://localhost:8080/api/clientes/com-endereco \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "nome": "João da Silva",
    "cpf": "12345678900",
    "email": "joao@example.com",
    "telefone": "11987654321",
    "endereco": {
      "rua": "Rua das Flores",
      "numero": "123",
      "complemento": "Apt 456",
      "bairro": "Centro",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01310100"
    }
  }'
```

### 📦 Produtos (`/produtos`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/produtos` | Lista todos os produtos | ✅ |
| GET | `/produtos/{id}` | Busca produto por ID | ✅ |
| GET | `/produtos/estoque-baixo` | Produtos com estoque baixo | ✅ |
| GET | `/produtos/health` | Health check do serviço | ❌ |
| POST | `/produtos` | Cria novo produto | ✅ |
| PUT | `/produtos/{id}` | Atualiza produto | ✅ |
| DELETE | `/produtos/{id}` | Remove produto | ✅ |
| POST | `/produtos/{id}/movimentacao` | Registra entrada/saída | ✅ |

**Exemplo de criar produto:**

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "nome": "Tomate",
    "descricao": "Tomate fresco",
    "preco": 5.50,
    "quantidadeEstoque": 100,
    "unidadeMedida": "kg"
  }'
```

### 🛒 Vendas (`/vendas`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/vendas` | Lista todas as vendas | ✅ |
| GET | `/vendas/{id}` | Busca venda por ID | ✅ |
| GET | `/vendas/numero/{numero}` | Busca venda por número | ✅ |
| GET | `/vendas/cliente/{clienteId}` | Vendas de um cliente | ✅ |
| GET | `/vendas/status/{status}` | Vendas por status | ✅ |
| GET | `/vendas/{id}/pdf` | Gera PDF da venda | ✅ |
| POST | `/vendas` | Cria nova venda | ✅ |
| PUT | `/vendas/{id}/finalizar` | Finaliza venda | ✅ |
| PUT | `/vendas/{id}/finalizar/pdf` | Finaliza e retorna PDF | ✅ |
| PUT | `/vendas/{id}/cancelar` | Cancela venda | ✅ |

**Exemplo de criar venda:**

```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "clienteId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 5,
        "precoUnitario": 5.50
      },
      {
        "produtoId": 2,
        "quantidade": 3,
        "precoUnitario": 8.00
      }
    ]
  }'
```

### 📊 Relatórios (`/relatorios`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/relatorios/vendas/completo` | Relatório completo | ✅ |
| GET | `/relatorios/vendas/resumo` | Relatório resumido | ✅ |
| GET | `/relatorios/vendas/cliente/{clienteId}` | Relatório por cliente | ✅ |
| GET | `/relatorios/vendas/produto/{produtoId}` | Relatório por produto | ✅ |
| GET | `/relatorios/health` | Health check | ❌ |

**Exemplo de gerar relatório:**

```bash
curl -X GET "http://localhost:8080/api/relatorios/vendas/completo?dataInicio=2024-01-01&dataFim=2024-12-31" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 📈 Dashboard (`/dashboard`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/dashboard/estatisticas` | Estatísticas gerais | ✅ |

---

## 🔐 Autenticação

### Fluxo JWT

1. **Login**: Cliente envia credenciais → servidor retorna `accessToken` (15 min) + `refreshToken` (7 dias)
2. **Requisições**: Cliente envia `Authorization: Bearer {accessToken}` no header
3. **Expiração**: Se token expirar, usar `refreshToken` para obter novo `accessToken`
4. **Logout**: Servidor invalida todos os `refreshToken` do usuário

### Usando o Token

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Estrutura do Token JWT (payload decodificado)

```json
{
  "sub": "user-uuid",
  "email": "admin@hortiflow.com",
  "role": "ADMIN",
  "iat": 1700000000,
  "exp": 1700000900
}
```

---

## 🗄️ Banco de Dados

### Entidades Principais

```
User (Usuário/Admin)
├── id (UUID, PK)
├── email (UNIQUE)
├── passwordHash (BCrypt)
├── role (ADMIN, USER)
├── isActive
└── createdAt

Cliente
├── id (Long, PK)
├── nome
├── cpf
├── email
├── telefone
└── enderecos (1-to-many → Endereco)

Endereco
├── id (Long, PK)
├── clienteId (FK)
├── rua, numero, bairro, cidade, estado, cep
└── tipo (RESIDENCIAL, COMERCIAL)

Produto
├── id (Long, PK)
├── nome
├── descricao
├── preco
├── quantidadeEstoque
├── nivelEstoqueMinimo
├── unidadeMedida
└── ativo

Venda
├── id (Long, PK)
├── numeroVenda (UNIQUE)
├── clienteId (FK → Cliente)
├── dataVenda
├── totalVenda
├── status (PENDENTE, FINALIZADA, CANCELADA)
├── formaPagamento
└── itens (1-to-many → VendaItem)

VendaItem
├── id (Long, PK)
├── vendaId (FK → Venda)
├── produtoId (FK → Produto)
├── quantidade
├── precoUnitario
└── subtotal
```

### Inicialização do Banco

O arquivo `application.properties` define:
```properties
spring.jpa.hibernate.ddl-auto=none
```

Isso significa que o Hibernate **não cria/altera** tabelas automaticamente. Para primeira utilização:

1. **Opção A (Manual)**: Execute os scripts SQL em `src/main/resources/db/migration/` (se existirem)
2. **Opção B (Automático com Flyway)**: Descomente a dependência Flyway em `pom.xml` e coloque scripts em `db/migration/`
3. **Opção C (Para desenvolvimento)**: Altere temporariamente para `ddl-auto=update` (⚠️ não recomendado em produção)

### Conexão ao Banco

Use uma ferramenta como **DBeaver** ou **pgAdmin** para inspecionar o banco:
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `hortiflow`
- **User**: `postgres` ou `hortiflow_user`
- **Password**: conforme configurado

---

## 🛠️ Ferramentas e Dependências

### Principais Dependências

| Dependência | Versão | Uso |
|-------------|--------|-----|
| Spring Boot | 3.3.4 | Framework principal |
| Spring Security | 3.3.4 | Autenticação e autorização |
| Spring Data JPA | 3.3.4 | ORM e persistência |
| PostgreSQL Driver | 42.6.0 | Driver BD PostgreSQL |
| JJWT (JSON Web Token) | 0.11.5 | Geração/validação JWT |
| Spring Validation | 3.3.4 | Validação com annotations |
| Swagger/OpenAPI | 2.6.0 | Documentação automática |
| iText | 7.2.5 | Geração de PDF |
| Lombok | 1.18.34 | Redução de boilerplate |

### Build e Testes

- **Maven**: Gerenciador de dependências e build
- **Surefire Plugin**: Execução de testes unitários
- **Javadoc Plugin**: Geração de documentação

---

## 🐛 Troubleshooting

### Problema: Porta 8080 já em uso

```powershell
# Windows - Encontrar e parar processo
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou alterar porta em application.properties
server.port=8081
```

### Problema: Conexão com PostgreSQL recusada

1. Verificar se PostgreSQL está rodando:
   ```powershell
   Get-Service postgresql-x64-*
   ```

2. Verificar credenciais em `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hortiflow
   spring.datasource.username=postgres
   spring.datasource.password=123456
   ```

3. Testar conexão:
   ```powershell
   psql -h localhost -U postgres -d hortiflow
   ```

### Problema: Erro no login (401 ou 500)

1. Verificar se usuário admin existe:
   ```sql
   SELECT * FROM public.user WHERE email = 'admin@hortiflow.com';
   ```

2. Se não existir, a classe `AdminSeed` deve criar automaticamente na primeira inicialização. Se não ocorrer:
   - Verificar logs: `logging.level.com.hortifruti=DEBUG`
   - Executar manualmente INSERT com senha hash (BCrypt)

3. Verificar JWT secret em `application.properties`:
   ```properties
   app.jwt.secret=change-me-please-32bytes-minimum-secret-key-123456
   ```

### Problema: CORS bloqueado no frontend

A segurança CORS está configurada em `SecurityConfig.java`:
```java
.cors(cors -> cors.configurationSource(request -> {
    var corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*"));
    corsConfig.setAllowedMethods(List.of("*"));
    corsConfig.setAllowedHeaders(List.of("*"));
    return corsConfig;
}))
```

Se ainda houver problema, verifique:
- O frontend está em `http://localhost:3002`?
- A API é acessível em `http://localhost:8080/api`?

### Problema: Erro ao gerar PDF

iText requer que as dependências estejam completas. Se falhar:

```powershell
# Limpar cache e reinstalar
mvn clean install -U
```

### Problema: Swagger não abre

Se `http://localhost:8080/api/swagger-ui/index.html` retornar 404:

1. Verificar logs para erros de inicialização
2. Confirmar que `springdoc-openapi-starter-webmvc-ui` está em `pom.xml`
3. Verificar se há conflito com `spring.mvc.static-path-pattern` em `application.properties`

---

## 📚 Recursos Adicionais

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Swagger/OpenAPI](https://swagger.io/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT.io](https://jwt.io/) — decoder para tokens

---

## 👥 Contribuindo

Se encontrar bugs ou tiver sugestões:

1. Abra uma issue no repositório
2. Descreva o problema e passos para reproduzir
3. Anexe logs relevantes

---

## 📄 Licença

[Adicionar licença aqui, ex: MIT]

---

## 📞 Suporte

Para dúvidas e suporte, contate: **[seu-email@example.com]**

---

**Última atualização**: Novembro de 2024  
**Versão Backend**: 1.0.0  
**Versão Spring Boot**: 3.3.4  
**Java**: 17+
