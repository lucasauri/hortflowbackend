package com.hortifruti.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Classe que representa um item de venda no sistema Hortifruti.
 * 
 * <p>Um item de venda contém informações sobre um produto específico vendido,
 * incluindo quantidade, preço unitário e subtotal calculado.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "itens_venda")
public class ItemVenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @JsonIgnore
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private Double quantidade;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Compatibilidade com esquema atual do banco (coluna NOT NULL total_item)
    @Column(name = "total_item", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalItem;
    
    /**
     * Construtor padrão da classe ItemVenda.
     */
    public ItemVenda() {}
    
    /**
     * Construtor com parâmetros principais.
     * 
     * <p>Calcula automaticamente o subtotal baseado no preço unitário e quantidade.
     * 
     * @param produto Produto vendido
     * @param quantidade Quantidade do produto
     * @param precoUnitario Preço unitário do produto
     */
    public ItemVenda(Produto produto, Double quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
    
    // Getters e Setters
    
    /**
     * Obtém o identificador único do item de venda.
     * 
     * @return ID do item
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador único do item de venda.
     * 
     * @param id ID do item
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Obtém a venda à qual este item pertence.
     * 
     * @return Venda associada
     */
    public Venda getVenda() {
        return venda;
    }
    
    /**
     * Define a venda à qual este item pertence.
     * 
     * @param venda Venda associada
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    
    /**
     * Obtém o produto vendido neste item.
     * 
     * @return Produto vendido
     */
    public Produto getProduto() {
        return produto;
    }
    
    /**
     * Define o produto vendido neste item.
     * 
     * @param produto Produto vendido
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    /**
     * Obtém a quantidade do produto vendido.
     * 
     * @return Quantidade vendida
     */
    public Double getQuantidade() {
        return quantidade;
    }
    
    /**
     * Define a quantidade do produto vendido.
     * 
     * @param quantidade Quantidade vendida
     */
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém o preço unitário do produto no item.
     */
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    /**
     * Define o preço unitário do produto no item.
     */
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /**
     * Obtém o subtotal do item (preço x quantidade).
     */
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /**
     * Define o subtotal do item (preço x quantidade).
     */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtém o total_item (compatível com coluna do banco).
     */
    public BigDecimal getTotalItem() {
        return totalItem;
    }

    /**
     * Define o total_item (compatível com coluna do banco).
     */
    public void setTotalItem(BigDecimal totalItem) {
        this.totalItem = totalItem;
    }
}