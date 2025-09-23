package com.hortifruti.model;

/**
 * Classe que representa um produto no sistema Hortifruti.
 * Implementa encapsulamento através de atributos privados e métodos públicos.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
public class Produto {
    
    /** Identificador único do produto */
    private Long id;
    
    /** Nome do produto */
    private String nome;
    
    /** Preço unitário do produto */
    private Double preco;
    
    /** Tipo de embalagem do produto */
    private String embalagem;
    
    /** Quantidade inicial em estoque */
    private Double estoqueInicial;
    
    /** Total de entradas no estoque */
    private Double entradas;
    
    /** Total de saídas do estoque */
    private Double saidas;
    
    /**
     * Construtor padrão da classe Produto.
     */
    public Produto() {
        this.embalagem = "Band. 200m";
        this.estoqueInicial = 0.0;
        this.entradas = 0.0;
        this.saidas = 0.0;
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param nome Nome do produto
     * @param preco Preço unitário do produto
     * @param embalagem Tipo de embalagem
     */
    public Produto(String nome, Double preco, String embalagem) {
        this();
        this.nome = nome;
        this.preco = preco;
        this.embalagem = embalagem;
    }
    
    /**
     * Construtor completo com todos os parâmetros.
     * 
     * @param id Identificador único
     * @param nome Nome do produto
     * @param preco Preço unitário
     * @param embalagem Tipo de embalagem
     * @param estoqueInicial Estoque inicial
     * @param entradas Total de entradas
     * @param saidas Total de saídas
     */
    public Produto(Long id, String nome, Double preco, String embalagem, 
                   Double estoqueInicial, Double entradas, Double saidas) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.embalagem = embalagem;
        this.estoqueInicial = estoqueInicial;
        this.entradas = entradas;
        this.saidas = saidas;
    }
    
    /**
     * Calcula o estoque atual do produto.
     * 
     * @return Quantidade atual em estoque
     */
    public Double getEstoqueAtual() {
        return estoqueInicial + entradas - saidas;
    }
    
    /**
     * Verifica se o produto está com estoque baixo (menos de 10 unidades).
     * 
     * @return true se o estoque estiver baixo, false caso contrário
     */
    public boolean isEstoqueBaixo() {
        return getEstoqueAtual() < 10.0;
    }
    
    /**
     * Calcula o valor total em estoque.
     * 
     * @return Valor total do estoque atual
     */
    public Double getValorEstoque() {
        return getEstoqueAtual() * preco;
    }
    
    // Getters e Setters
    
    /**
     * Obtém o identificador do produto.
     * 
     * @return ID do produto
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador do produto.
     * 
     * @param id ID do produto
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome do produto.
     * 
     * @return Nome do produto
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do produto.
     * 
     * @param nome Nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém o preço do produto.
     * 
     * @return Preço unitário
     */
    public Double getPreco() {
        return preco;
    }
    
    /**
     * Define o preço do produto.
     * 
     * @param preco Preço unitário
     */
    public void setPreco(Double preco) {
        this.preco = preco;
    }
    
    /**
     * Obtém a embalagem do produto.
     * 
     * @return Tipo de embalagem
     */
    public String getEmbalagem() {
        return embalagem;
    }
    
    /**
     * Define a embalagem do produto.
     * 
     * @param embalagem Tipo de embalagem
     */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }
    
    /**
     * Obtém o estoque inicial.
     * 
     * @return Quantidade inicial em estoque
     */
    public Double getEstoqueInicial() {
        return estoqueInicial;
    }
    
    /**
     * Define o estoque inicial.
     * 
     * @param estoqueInicial Quantidade inicial em estoque
     */
    public void setEstoqueInicial(Double estoqueInicial) {
        this.estoqueInicial = estoqueInicial;
    }
    
    /**
     * Obtém o total de entradas.
     * 
     * @return Total de entradas no estoque
     */
    public Double getEntradas() {
        return entradas;
    }
    
    /**
     * Define o total de entradas.
     * 
     * @param entradas Total de entradas no estoque
     */
    public void setEntradas(Double entradas) {
        this.entradas = entradas;
    }
    
    /**
     * Obtém o total de saídas.
     * 
     * @return Total de saídas do estoque
     */
    public Double getSaidas() {
        return saidas;
    }
    
    /**
     * Define o total de saídas.
     * 
     * @param saidas Total de saídas do estoque
     */
    public void setSaidas(Double saidas) {
        this.saidas = saidas;
    }
    
    /**
     * Adiciona uma entrada ao estoque.
     * 
     * @param quantidade Quantidade a ser adicionada
     */
    public void adicionarEntrada(Double quantidade) {
        this.entradas += quantidade;
    }
    
    /**
     * Adiciona uma saída ao estoque.
     * 
     * @param quantidade Quantidade a ser removida
     */
    public void adicionarSaida(Double quantidade) {
        this.saidas += quantidade;
    }
    
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", embalagem='" + embalagem + '\'' +
                ", estoqueAtual=" + getEstoqueAtual() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return id != null && id.equals(produto.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 