package com.hortifruti.service;

import com.hortifruti.model.*;
import com.hortifruti.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Transactional
    public Venda criarVenda(Venda venda) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(venda.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        if (venda.getEnderecoEntrega() != null && venda.getEnderecoEntrega().getId() != null) {
            Long enderecoId = venda.getEnderecoEntrega().getId();
            Endereco endereco = enderecoRepository.findById(enderecoId)
                    .orElseThrow(() -> new RuntimeException("Endereço de entrega não encontrado"));
            if (!endereco.getCliente().getId().equals(cliente.getId())) {
                throw new RuntimeException("Endereço de entrega não pertence ao cliente informado");
            }
            venda.setEnderecoEntrega(endereco);
        } else {
            // Fallback: se o front não enviar endereço, usar o principal do cliente (se existir)
            List<Endereco> enderecos = enderecoRepository.findByClienteIdOrderByPrincipalDesc(cliente.getId());
            if (enderecos != null && !enderecos.isEmpty()) {
                venda.setEnderecoEntrega(enderecos.get(0));
            }
        }
        
        // Gerar número da venda
        String numeroVenda = gerarNumeroVenda();
        venda.setNumeroVenda(numeroVenda);
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus(Venda.StatusVenda.PENDENTE);
        
        // Validar e processar itens
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve conter pelo menos um item");
        }
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (ItemVenda item : venda.getItens()) {
            // Validar produto
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduto().getId()));
            
            // Validar estoque
            if (produto.getEstoqueAtual() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            
            // Configurar item
            item.setProduto(produto);
            item.setVenda(venda);
            item.setPrecoUnitario(BigDecimal.valueOf(produto.getPreco()));
            item.setSubtotal(BigDecimal.valueOf(produto.getPreco()).multiply(BigDecimal.valueOf(item.getQuantidade())));
            // Preencher totalItem para compatibilidade com coluna NOT NULL
            item.setTotalItem(item.getSubtotal());
            
            valorTotal = valorTotal.add(item.getSubtotal());
            
            // Atualizar estoque do produto (aumentar saídas)
            produto.setSaidas(produto.getSaidas() + item.getQuantidade().doubleValue());
            produtoRepository.save(produto);
            
            // Registrar movimentação de estoque
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProdutoId(produto.getId());
            movimentacao.setTipo(TipoMovimentacao.SAIDA.name());
            movimentacao.setQuantidade(item.getQuantidade().doubleValue());
            movimentacao.setData(LocalDateTime.now());
            movimentacaoEstoqueRepository.save(movimentacao);
        }
        
        // Calcular valores finais
        venda.setValorTotal(valorTotal);
        
        BigDecimal desconto = venda.getDesconto() != null ? venda.getDesconto() : BigDecimal.ZERO;
        BigDecimal valorFinal = valorTotal.subtract(desconto);
        venda.setValorFinal(valorFinal);
        
        // Salvar venda
        return vendaRepository.save(venda);
    }
    
    @Transactional
    public Venda finalizarVenda(Long vendaId, String formaPagamento) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
        
        if (venda.getStatus() != Venda.StatusVenda.PENDENTE) {
            throw new RuntimeException("Apenas vendas pendentes podem ser finalizadas");
        }
        
        venda.setStatus(Venda.StatusVenda.FINALIZADA);
        venda.setFormaPagamento(formaPagamento);
        
        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda finalizarVendaPorNumero(String numeroVenda, String formaPagamento) {
        Venda venda = vendaRepository.findByNumeroVenda(numeroVenda)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (venda.getStatus() != Venda.StatusVenda.PENDENTE) {
            throw new RuntimeException("Apenas vendas pendentes podem ser finalizadas");
        }

        venda.setStatus(Venda.StatusVenda.FINALIZADA);
        venda.setFormaPagamento(formaPagamento);

        return vendaRepository.save(venda);
    }
    
    @Transactional
    public Venda cancelarVenda(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
        
        if (venda.getStatus() != Venda.StatusVenda.PENDENTE) {
            throw new RuntimeException("Apenas vendas pendentes podem ser canceladas");
        }
        
        // Devolver produtos ao estoque (reduzir saídas)
        for (ItemVenda item : venda.getItens()) {
            Produto produto = item.getProduto();
            produto.setSaidas(produto.getSaidas() - item.getQuantidade().doubleValue());
            produtoRepository.save(produto);
            
            // Registrar movimentação de estoque
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setProdutoId(produto.getId());
            movimentacao.setTipo(TipoMovimentacao.ENTRADA.name());
            movimentacao.setQuantidade(item.getQuantidade().doubleValue());
            movimentacao.setData(LocalDateTime.now());
            movimentacaoEstoqueRepository.save(movimentacao);
        }
        
        venda.setStatus(Venda.StatusVenda.CANCELADA);
        return vendaRepository.save(venda);
    }
    
    public List<Venda> listarTodas() {
        return vendaRepository.findAllOrderByDataVendaDesc();
    }
    
    public List<Venda> listarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteIdOrderByDataVendaDesc(clienteId);
    }
    
    public List<Venda> listarPorStatus(Venda.StatusVenda status) {
        return vendaRepository.findByStatusOrderByDataVendaDesc(status);
    }
    
    public Optional<Venda> buscarPorId(Long id) {
        return vendaRepository.findById(id);
    }
    
    public Optional<Venda> buscarPorNumero(String numeroVenda) {
        return vendaRepository.findByNumeroVenda(numeroVenda);
    }
    
    private String gerarNumeroVenda() {
        String data = LocalDateTime.now().toString().replaceAll("[^0-9]", "");
        String uuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "VND" + data + uuid;
    }
}