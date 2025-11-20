package com.hortifruti.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe que representa uma venda no sistema Hortifruti.
 * 
 * <p>Uma venda contém informações sobre o cliente, data, valores, forma de pagamento
 * e uma lista de itens vendidos. O sistema gerencia o status da venda (PENDENTE, 
 * FINALIZADA, CANCELADA) e calcula automaticamente os valores totais.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "vendas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    /** Endereço de entrega selecionado para a venda (pertence ao cliente) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id")
    private Endereco enderecoEntrega;
    
    @Column(nullable = false)
    private LocalDateTime dataVenda;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal desconto;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVenda status;
    
    @Column(nullable = false)
    private String formaPagamento;
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemVenda> itens;
    
    @Column(nullable = true, length = 500)
    private String observacoes;
    
    @Column(nullable = true, unique = true)
    private String numeroVenda;
    
    /**
     * Construtor padrão da classe Venda.
     * 
     * <p>Inicializa a venda com data atual, status PENDENTE e desconto zero.
     */
    public Venda() {
        this.dataVenda = LocalDateTime.now();
        this.status = StatusVenda.PENDENTE;
        this.desconto = BigDecimal.ZERO;
    }
    
    /**
     * Enumeração que representa os possíveis status de uma venda.
     * 
     * <ul>
     *   <li>PENDENTE - Venda criada mas ainda não finalizada</li>
     *   <li>FINALIZADA - Venda concluída e paga</li>
     *   <li>CANCELADA - Venda cancelada (produtos devolvidos ao estoque)</li>
     * </ul>
     */
    public enum StatusVenda {
        /** Venda pendente de finalização */
        PENDENTE, 
        /** Venda finalizada e paga */
        FINALIZADA, 
        /** Venda cancelada */
        CANCELADA
    }
    
    // Getters e Setters
    
    /**
     * Obtém o identificador único da venda.
     * 
     * @return ID da venda
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador único da venda.
     * 
     * @param id ID da venda
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Obtém o cliente associado à venda.
     * 
     * @return Cliente da venda
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    /**
     * Define o cliente associado à venda.
     * 
     * @param cliente Cliente da venda
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Obtém o endereço de entrega da venda.
     * 
     * @return Endereço de entrega
     */
    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }
    
    /**
     * Define o endereço de entrega da venda.
     * 
     * @param enderecoEntrega Endereço de entrega (deve pertencer ao cliente)
     */
    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
    
    /**
     * Obtém a data e hora da venda.
     * 
     * @return Data e hora da venda
     */
    public LocalDateTime getDataVenda() {
        return dataVenda;
    }
    
    /**
     * Define a data e hora da venda.
     * 
     * @param dataVenda Data e hora da venda
     */
    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    /**
     * Obtém o valor total da venda (antes do desconto).
     * 
     * @return Valor total da venda
     */
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    /**
     * Define o valor total da venda (antes do desconto).
     * 
     * @param valorTotal Valor total da venda
     */
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    /**
     * Obtém o valor do desconto aplicado à venda.
     * 
     * @return Valor do desconto
     */
    public BigDecimal getDesconto() {
        return desconto;
    }
    
    /**
     * Define o valor do desconto aplicado à venda.
     * 
     * @param desconto Valor do desconto
     */
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }
    
    /**
     * Obtém o valor final da venda (após desconto).
     * 
     * @return Valor final da venda
     */
    public BigDecimal getValorFinal() {
        return valorFinal;
    }
    
    /**
     * Define o valor final da venda (após desconto).
     * 
     * @param valorFinal Valor final da venda
     */
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
    
    /**
     * Obtém o status atual da venda.
     * 
     * @return Status da venda
     */
    public StatusVenda getStatus() {
        return status;
    }
    
    /**
     * Define o status da venda.
     * 
     * @param status Status da venda
     */
    public void setStatus(StatusVenda status) {
        this.status = status;
    }
    
    /**
     * Obtém a forma de pagamento utilizada na venda.
     * 
     * @return Forma de pagamento
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    /**
     * Define a forma de pagamento utilizada na venda.
     * 
     * @param formaPagamento Forma de pagamento
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    /**
     * Obtém a lista de itens da venda.
     * 
     * @return Lista de itens da venda
     */
    public List<ItemVenda> getItens() {
        return itens;
    }
    
    /**
     * Define a lista de itens da venda.
     * 
     * @param itens Lista de itens da venda
     */
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
    
    /**
     * Obtém as observações da venda.
     * 
     * @return Observações da venda
     */
    public String getObservacoes() {
        return observacoes;
    }
    
    /**
     * Define as observações da venda.
     * 
     * @param observacoes Observações da venda
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    /**
     * Obtém o número único da venda.
     * 
     * @return Número da venda
     */
    public String getNumeroVenda() {
        return numeroVenda;
    }
    
    /**
     * Define o número único da venda.
     * 
     * @param numeroVenda Número da venda
     */
    public void setNumeroVenda(String numeroVenda) {
        this.numeroVenda = numeroVenda;
    }
}