# HortiFlow - Sistema de Gestão de Hortifrúti

Este repositório contém uma aplicação completa (Backend em Spring Boot + Frontend em HTML/CSS/JS) para gestão de Produtos, Clientes e controle básico de Estoque.

- Backend (Java/Spring Boot): `backend/`
- Frontend (HTML/CSS/JS): `frontend/`
- Banco de dados: PostgreSQL
- Documentação de API: Swagger UI

---

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- PostgreSQL (com um banco chamado `hortiflow`)

### Configuração do Banco
Ajuste as credenciais conforme necessário em `backend/src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/hortiflow
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

Crie o banco (se necessário):

```
CREATE DATABASE hortiflow;
```

### Rodar o Backend
- Via script: `backend/run.bat`
- Ou via Maven (na pasta `backend/`):

```
mvn clean spring-boot:run
```

Endpoints e documentação:
- API base: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui/index.html

### Rodar o Frontend
- Via script: `frontend/start-frontend.bat`
- Acesse: http://localhost:3000
- O frontend consome a API de `http://localhost:8080/api`

---

## Funcionalidades

- CRUD de Produtos (listar, criar, editar, excluir)
- Movimentação de estoque (Entrada/Saída) com validação de estoque insuficiente
- Dashboard com estatísticas de estoque e produtos
- CRUD de Clientes (listar, criar, editar, excluir)
- Swagger UI para documentação e teste da API
- Frontend SPA simples com toasts, loading overlay, filtros e buscas

---

## Estrutura do Projeto

```
ProjetoHortiflow/
├─ backend/
│  ├─ src/main/java/com/hortifruti/
│  │  ├─ controller/
│  │  │  ├─ ProdutoController.java
│  │  │  └─ ClienteController.java
│  │  ├─ service/
│  │  │  ├─ ProdutoService.java
│  │  │  └─ ClienteService.java
│  │  ├─ repository/
│  │  │  ├─ ProdutoRepository.java
│  │  │  └─ ClienteRepository.java
│  │  ├─ model/
│  │  │  ├─ Produto.java
│  │  │  └─ Cliente.java
│  │  └─ HortifrutiApplication.java
│  ├─ src/main/resources/
│  │  └─ application.properties
│  └─ run.bat
└─ frontend/
   ├─ index.html
   ├─ app.js
   ├─ styles.css
   └─ start-frontend.bat
```

---

## Endpoints Principais (Resumo)

### Produtos - `/api/produtos`
- GET `/api/produtos`
- GET `/api/produtos/{id}`
- POST `/api/produtos`
  - Exemplo:
    ```json
    {
      "nome": "Tomate",
      "preco": 4.5,
      "embalagem": "Bandeja",
      "estoqueInicial": 10
    }
    ```
- PUT `/api/produtos/{id}`
- DELETE `/api/produtos/{id}`
- POST `/api/produtos/{id}/movimentacao`
  - Body:
    ```json
    { "tipo": "ENTRADA", "quantidade": 5 }
    ```
- GET `/api/produtos/estoque-baixo`
- GET `/api/produtos/estatisticas`
- GET `/api/produtos/health`

### Clientes - `/api/clientes`
- GET `/api/clientes`
- GET `/api/clientes/{id}`
- POST `/api/clientes`
  - Exemplo:
    ```json
    {
      "nome": "José da Silva",
      "estado": "SP",
      "telefone": "11 99999-0000",
      "cnpj": "00.000.000/0001-00",
      "ie": "123456",
      "condPgto": "30 dias",
      "banco": "Itaú"
    }
    ```
- PUT `/api/clientes/{id}`
- DELETE `/api/clientes/{id}`

---

## Atendimento aos Requisitos (Programação II)

1. **Implementação do projeto proposto (1,0)**
- Sistema completo com CRUD de Produtos e Clientes, Dashboard de estoque e integração front-backend.

2. **POO: encapsulamento, herança e polimorfismo (1,0)**
- Encapsulamento nas entidades `Produto` e `Cliente` com atributos privados e getters/setters.
- Herança via `JpaRepository` (repositórios estendem a interface do Spring Data, herdando comportamentos).
- Polimorfismo através de injeção de dependência e uso de interfaces (controllers usam serviços; serviços usam repositórios). 
- Opcional para reforçar a herança: adicionar uma superclasse `Pessoa` e fazer `Cliente extends Pessoa`.

3. **JavaDoc (1,0)**
- Controllers e Services possuem JavaDoc descrevendo responsabilidade e endpoints.
- Entidades com comentários explicativos.
- Pode-se expandir ainda mais se necessário (ex.: todos os métodos públicos).

4. **Legibilidade, clareza e organização (1,0)**
- Pacotes por camada (controller, service, repository, model).
- Convenções Java (PascalCase para classes, camelCase para métodos/atributos).
- Métodos coesos e validações com mensagens claras (ex.: `ProdutoService.validarProduto`).

5. **Persistência dos dados (1,0)**
- JPA/Hibernate + PostgreSQL.
- `@Entity` + `@Table` nos modelos (`Produto`, `Cliente`).
- `ddl-auto=update` para criação/atualização automática de tabelas.

6. **Acréscimo de funcionalidades (1,0)**
- Dashboard (`ProdutoService.obterEstatisticas`) com totais e produtos de baixo estoque.
- Movimentação de estoque com validação de estoque insuficiente.
- Swagger UI e melhorias de UX no frontend (toasts, loading overlay, filtros e buscas).

7. **Versionamento GitHub (1,0)**
- Recomendações:
  - Criar repositório Git.
  - Commits semânticos (`feat:`, `fix:`, `docs:`).
  - README (este arquivo) e screenshots das telas/Swagger.

8. **Estrutura e formatação do artigo (1,0)**
- Sugestão de estrutura:
  - Introdução, Tecnologias, Arquitetura (camadas), Modelagem, POO, Persistência, API (prints do Swagger), Frontend (prints), Extras/Validações, Conclusão.

9. **Apresentação oral (1,0)**
- Roteiro sugerido:
  - Introdução (objetivo e arquitetura).
  - Demo: Dashboard → CRUD Produtos → movimentação → CRUD Clientes → Swagger.
  - POO e persistência.
  - Encerramento com próximos passos.

10. **Autoavaliação (1,0)**
- Pontos fortes: arquitetura em camadas, persistência real, documentação, UX do frontend, validações.
- A melhorar: testes automatizados, histórico detalhado de movimentações, relatórios.
- Aprendizados: JPA, REST, front SPA simples, integração com DB.

---

## Referências de Código

- Backend
  - `controller/ProdutoController.java`, `controller/ClienteController.java`
  - `service/ProdutoService.java`, `service/ClienteService.java`
  - `repository/ProdutoRepository.java`, `repository/ClienteRepository.java`
  - `model/Produto.java`, `model/Cliente.java`
  - `resources/application.properties`
- Frontend
  - `index.html`, `app.js`, `styles.css`

---

## Dicas para a Demo

- Inicie PostgreSQL e verifique o banco `hortiflow`.
- Suba o backend e abra o Swagger UI.
- Suba o frontend e recarregue com `Ctrl+F5` (cache).
- Fluxo para apresentar:
  - Criar 2–3 produtos com estoques e preços diferentes.
  - Realizar entradas/saídas (mostrar validação na saída sem estoque suficiente).
  - Ver o Dashboard atualizado.
  - Criar e editar clientes.
  - Mostrar uma chamada via Swagger (POST/GET produtos).

---

## Próximos Passos (Roadmap Sugerido)

- Implementar listagem de movimentações de estoque (endpoint e UI).
- Adicionar entidade `Pedido` e `ItemPedido` com relacionamento e relatório de vendas.
- Criar testes unitários e de integração.
- Pipeline CI/CD simples com GitHub Actions.
