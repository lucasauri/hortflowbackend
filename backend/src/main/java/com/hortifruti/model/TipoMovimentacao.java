package com.hortifruti.model;

/**
 * Enum que representa os tipos de movimentação de estoque.
 */
public enum TipoMovimentacao {
    /**
     * Representa uma entrada de estoque.
     */
    ENTRADA("Entrada"),
    /**
     * Representa uma saída de estoque.
     */
    SAIDA("Saída"),
    /**
     * Representa o estoque inicial.
     */
    INICIAL("Inicial");
    
    private final String descricao;
    
    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Retorna a descrição do tipo de movimentação.
     * @return A descrição da movimentação.
     */
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}