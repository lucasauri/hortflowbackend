package com.hortifruti.controller;

import com.hortifruti.dto.ClienteEnderecoRequest;
import com.hortifruti.dto.ClienteEnderecoResponse;
import com.hortifruti.model.Cliente;
import com.hortifruti.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller REST para gerenciamento de clientes.
 * 
 * <p>Fornece endpoints para CRUD completo de clientes, incluindo
 * validação de dados e tratamento de erros.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    /**
     * Construtor para injeção de dependências.
     * @param clienteService Serviço de clientes
     */
    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Retorna uma lista de todos os clientes.
     * @return Lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodos() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    /**
     * Busca um cliente pelo seu ID.
     * @param id O ID do cliente
     * @return O cliente, se encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo cliente.
     * @param cliente O cliente a ser criado
     * @return O cliente criado
     */
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        Cliente criado = clienteService.criar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    /**
     * Cria um novo cliente juntamente com seu endereço principal.
     */
    @PostMapping("/com-endereco")
    public ResponseEntity<ClienteEnderecoResponse> criarComEndereco(@Valid @RequestBody ClienteEnderecoRequest request) {
        // Log minimal info to help debugging bindings
        try {
            logger.debug("Criar cliente request - nome: '{}', cpf: '{}', endereco-present: {}", request.getNome(), request.getCpf(), request.getEndereco() != null);
        } catch (Exception e) {
            logger.warn("Erro ao logar request de cliente: {}", e.getMessage());
        }

        ClienteEnderecoResponse resp = clienteService.criarComEndereco(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /**
     * Atualiza um cliente existente.
     * @param id O ID do cliente a ser atualizado
     * @param cliente O cliente com os dados atualizados
     * @return O cliente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return clienteService.atualizar(id, cliente)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove um cliente pelo seu ID.
     * @param id O ID do cliente a ser removido
     * @return Resposta vazia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (clienteService.remover(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
