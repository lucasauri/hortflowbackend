package com.hortifruti.service;

import com.hortifruti.dto.ClienteEnderecoRequest;
import com.hortifruti.dto.ClienteEnderecoResponse;
import com.hortifruti.model.Cliente;
import com.hortifruti.model.Endereco;
import com.hortifruti.repository.ClienteRepository;
import com.hortifruti.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para lógica de negócios de clientes.
 */
@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    /**
     * Construtor para injeção de dependências.
     * @param clienteRepository Repositório de clientes
     */
    @Autowired
    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * Retorna uma lista de todos os clientes, ordenados por nome.
     * @return Lista de clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAllSortedByNome();
    }

    /**
     * Busca um cliente pelo seu ID.
     * @param id O ID do cliente
     * @return Um Optional contendo o cliente, se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Cria um novo cliente.
     * @param cliente O cliente a ser criado
     * @return O cliente criado
     */
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

    /**
     * Cria um novo cliente e, opcionalmente, um endereço associado (principal).
     */
    public ClienteEnderecoResponse criarComEndereco(ClienteEnderecoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Requisição não pode ser nula");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setCpf(request.getCpf());
        cliente.setEstado(request.getEstado());
        cliente.setTelefone(request.getTelefone());
        cliente.setCnpj(request.getCnpj());
        cliente.setIe(request.getIe());
        cliente.setCondPgto(request.getCondPgto());
        cliente.setBanco(request.getBanco());

        Cliente clienteCriado = criar(cliente);

        Endereco enderecoCriado = null;
        if (request.getEndereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setCliente(clienteCriado);
            endereco.setRua(request.getEndereco().getRua());
            endereco.setNumero(request.getEndereco().getNumero());
            endereco.setComplemento(request.getEndereco().getComplemento());
            endereco.setBairro(request.getEndereco().getBairro());
            endereco.setCidade(request.getEndereco().getCidade());
            endereco.setEstado(request.getEndereco().getEstado());
            endereco.setCep(request.getEndereco().getCep());
            // Se não informado, define como principal
            boolean principal = request.getEndereco().getPrincipal() == null || request.getEndereco().getPrincipal();
            endereco.setPrincipal(principal);

            enderecoCriado = enderecoRepository.save(endereco);
        }

        return new ClienteEnderecoResponse(clienteCriado, enderecoCriado);
    }

    /**
     * Atualiza um cliente existente.
     * @param id O ID do cliente a ser atualizado
     * @param cliente O cliente com os dados atualizados
     * @return Um Optional contendo o cliente atualizado, se encontrado
     */
    public Optional<Cliente> atualizar(Long id, Cliente cliente) {
        validarCliente(cliente);
        if (!clienteRepository.existsById(id)) {
            return Optional.empty();
        }
        cliente.setId(id);
        Cliente atualizado = clienteRepository.save(cliente);
        return Optional.of(atualizado);
    }

    /**
     * Remove um cliente pelo seu ID.
     * @param id O ID do cliente a ser removido
     * @return true se o cliente foi removido, false caso contrário
     */
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
