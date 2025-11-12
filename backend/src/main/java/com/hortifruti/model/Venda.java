package com.hortifruti.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
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
    
    public Venda() {
        this.dataVenda = LocalDateTime.now();
        this.status = StatusVenda.PENDENTE;
        this.desconto = BigDecimal.ZERO;
    }
    
    public enum StatusVenda {
        PENDENTE, FINALIZADA, CANCELADA
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public LocalDateTime getDataVenda() {
        return dataVenda;
    }
    
    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getDesconto() {
        return desconto;
    }
    
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }
    
    public BigDecimal getValorFinal() {
        return valorFinal;
    }
    
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
    
    public StatusVenda getStatus() {
        return status;
    }
    
    public void setStatus(StatusVenda status) {
        this.status = status;
    }
    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    public List<ItemVenda> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public String getNumeroVenda() {
        return numeroVenda;
    }
    
    public void setNumeroVenda(String numeroVenda) {
        this.numeroVenda = numeroVenda;
    }
}