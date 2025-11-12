package com.hortifruti.service;

import com.hortifruti.model.Cliente;
import com.hortifruti.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAllSortedByNome();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente criar(Cliente cliente) {
        validarCliente(cliente);
        
        // Garante que o ID seja null para criação (não atualização)
        // Se vier como 0 ou qualquer outro valor, força para null
        if (cliente.getId() == null || cliente.getId() == 0) {
            cliente.setId(null);
        } else {
            // Se tem ID não-nulo, é uma tentativa de atualização, não permitir
            throw new IllegalArgumentException("ID não deve ser fornecido para criação de novo cliente");
        }
        
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            // Log detalhado do erro antes de relançar
            System.err.println("Erro ao salvar cliente:");
            System.err.println("ID: " + cliente.getId());
            System.err.println("Nome: " + cliente.getNome());
            System.err.println("CPF: " + cliente.getCpf());
            System.err.println("Estado: " + cliente.getEstado());
            throw new RuntimeException("Erro ao salvar cliente no banco de dados: " + e.getMessage(), e);
        }
    }

    public Optional<Cliente> atualizar(Long id, Cliente cliente) {
        validarCliente(cliente);
        if (!clienteRepository.existsById(id)) {
            return Optional.empty();
        }
        cliente.setId(id);
        Cliente atualizado = clienteRepository.save(cliente);
        return Optional.of(atualizado);
    }

    public boolean remover(Long id) {
        if (!clienteRepository.existsById(id)) return false;
        clienteRepository.deleteById(id);
        return true;
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
    }
}
