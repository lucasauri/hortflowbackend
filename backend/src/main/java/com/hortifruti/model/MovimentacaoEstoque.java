package com.hortifruti.model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma movimentação de estoque no sistema Hortifruti.
 * Implementa encapsulamento através de atributos privados e métodos públicos.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
public class MovimentacaoEstoque {
    
    /** Identificador único da movimentação */
    private Long id;
    
    /** Produto relacionado à movimentação */
    private Long produtoId;
    
    /** Tipo da movimentação (ENTRADA, SAIDA, INICIAL) */
    private String tipo;
    
    /** Quantidade movimentada */
    private Double quantidade;
    
    /** Data e hora da movimentação */
    private LocalDateTime data;
    
    /**
     * Construtor padrão da classe MovimentacaoEstoque.
     */
    public MovimentacaoEstoque() {
        this.data = LocalDateTime.now();
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param produtoId ID do produto
     * @param tipo Tipo da movimentação
     * @param quantidade Quantidade movimentada
     */
    public MovimentacaoEstoque(Long produtoId, String tipo, Double quantidade) {
        this();
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
    }
    
    /**
     * Construtor completo com todos os parâmetros.
     * 
     * @param id Identificador único
     * @param produtoId ID do produto
     * @param tipo Tipo da movimentação
     * @param quantidade Quantidade movimentada
     * @param data Data e hora da movimentação
     */
    public MovimentacaoEstoque(Long id, Long produtoId, String tipo, 
                               Double quantidade, LocalDateTime data) {
        this.id = id;
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
    }
    
    /**
     * Verifica se a movimentação é uma entrada.
     * 
     * @return true se for entrada, false caso contrário
     */
    public boolean isEntrada() {
        return "ENTRADA".equals(tipo);
    }
    
    /**
     * Verifica se a movimentação é uma saída.
     * 
     * @return true se for saída, false caso contrário
     */
    public boolean isSaida() {
        return "SAIDA".equals(tipo);
    }
    
    /**
     * Verifica se a movimentação é inicial.
     * 
     * @return true se for inicial, false caso contrário
     */
    public boolean isInicial() {
        return "INICIAL".equals(tipo);
    }
    
    /**
     * Obtém o sinal da movimentação (positivo para entrada, negativo para saída).
     * 
     * @return 1 para entrada, -1 para saída, 0 para inicial
     */
    public int getSinal() {
        if (isEntrada()) return 1;
        if (isSaida()) return -1;
        return 0;
    }
    
    // Getters e Setters
    
    /**
     * Obtém o identificador da movimentação.
     * 
     * @return ID da movimentação
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador da movimentação.
     * 
     * @param id ID da movimentação
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Obtém o ID do produto.
     * 
     * @return ID do produto
     */
    public Long getProdutoId() {
        return produtoId;
    }
    
    /**
     * Define o ID do produto.
     * 
     * @param produtoId ID do produto
     */
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
    
    /**
     * Obtém o tipo da movimentação.
     * 
     * @return Tipo da movimentação
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Define o tipo da movimentação.
     * 
     * @param tipo Tipo da movimentação
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /**
     * Obtém a quantidade movimentada.
     * 
     * @return Quantidade movimentada
     */
    public Double getQuantidade() {
        return quantidade;
    }
    
    /**
     * Define a quantidade movimentada.
     * 
     * @param quantidade Quantidade movimentada
     */
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
    
    /**
     * Obtém a data e hora da movimentação.
     * 
     * @return Data e hora da movimentação
     */
    public LocalDateTime getData() {
        return data;
    }
    
    /**
     * Define a data e hora da movimentação.
     * 
     * @param data Data e hora da movimentação
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "MovimentacaoEstoque{" +
                "id=" + id +
                ", produtoId=" + produtoId +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", data=" + data +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MovimentacaoEstoque that = (MovimentacaoEstoque) obj;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 