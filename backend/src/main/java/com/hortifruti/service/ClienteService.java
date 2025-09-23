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
        return clienteRepository.save(cliente);
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
