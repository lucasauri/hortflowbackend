# HortiFlow Backend - Sistema de Gestão

## 📋 Descrição

Backend Java para o sistema de gestão HortiFlow, desenvolvido com Spring Boot e seguindo as melhores práticas de Programação Orientada a Objetos (POO).

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/hortifruti/
├── controller/     # Controllers REST
├── service/        # Camada de lógica de negócio
├── dao/           # Data Access Objects
├── model/         # Entidades/Modelos
└── HortifrutiApplication.java  # Classe principal
```

## 🛠️ Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework para aplicações Java
- **Spring Data JPA** - Persistência de dados
- **SQLite** - Banco de dados
- **Maven** - Gerenciador de dependências
- **Jackson** - Processamento JSON
- **Lombok** - Redução de código boilerplate

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd hortifruti-desktop/backend
```

### 2. Compile o projeto
```bash
mvn clean compile
```

### 3. Execute a aplicação
```bash
mvn spring-boot:run
```

### 4. Acesse a API
- **URL Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/produtos/health

## 📚 Endpoints da API

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Lista todos os produtos |
| GET | `/api/produtos/{id}` | Busca produto por ID |
| GET | `/api/produtos/estoque-baixo` | Produtos com estoque baixo |
| GET | `/api/produtos/estatisticas` | Estatísticas gerais |
| POST | `/api/produtos` | Cria novo produto |
| PUT | `/api/produtos/{id}` | Atualiza produto |
| DELETE | `/api/produtos/{id}` | Remove produto |
| POST | `/api/produtos/{id}/movimentacao` | Adiciona movimentação de estoque |

### Exemplo de Uso

#### Criar Produto
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tomate",
    "preco": 5.50,
    "embalagem": "Band. 200m",
    "estoqueInicial": 100.0
  }'
```

#### Listar Produtos
```bash
curl http://localhost:8080/api/produtos
```

#### Adicionar Movimentação
```bash
curl -X POST http://localhost:8080/api/produtos/1/movimentacao \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "ENTRADA",
    "quantidade": 50.0
  }'
```

## 🗄️ Banco de Dados

O sistema utiliza SQLite como banco de dados, que é criado automaticamente na primeira execução.

### Tabelas

- **produtos**: Cadastro de produtos
- **clientes**: Cadastro de clientes
- **movimentacoes_estoque**: Histórico de movimentações
- **pedidos**: Pedidos realizados
- **itens_pedido**: Itens dos pedidos

## 📖 Conceitos de POO Implementados

### 1. Encapsulamento
- Atributos privados com getters e setters públicos
- Controle de acesso aos dados

### 2. Herança
- Estrutura preparada para extensão de classes

### 3. Polimorfismo
- Uso de interfaces e classes abstratas
- Sobrescrita de métodos

### 4. Abstração
- Separação clara de responsabilidades
- Interfaces bem definidas

## 📝 Documentação JavaDOC

Todas as classes e métodos possuem documentação JavaDOC completa, incluindo:

- Descrição da classe/método
- Parâmetros (@param)
- Valores de retorno (@return)
- Exceções (@throws)
- Autor e versão (@author, @version)

## 🧪 Testes

Para executar os testes:

```bash
mvn test
```

## 📦 Build

Para gerar o JAR executável:

```bash
mvn clean package
```

O arquivo JAR será gerado em `target/hortifruti-backend-1.0.0.jar`

## 🔧 Configurações

As configurações estão no arquivo `application.properties`:

- **Porta**: 8080
- **Context Path**: /api
- **Banco**: SQLite (hortifruti.db)
- **CORS**: Habilitado para todas as origens
- **Logging**: Configurado para debug

## 📊 Funcionalidades

- ✅ CRUD completo de produtos
- ✅ Controle de estoque
- ✅ Movimentações de entrada e saída
- ✅ Estatísticas gerais
- ✅ Validação de dados
- ✅ Tratamento de erros
- ✅ Documentação JavaDOC
- ✅ API RESTful
- ✅ Persistência de dados

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.

## 👥 Autores

- **Hortifruti Team** - Desenvolvimento inicial

## 📞 Suporte

Para dúvidas ou suporte, entre em contato através dos issues do repositório. 