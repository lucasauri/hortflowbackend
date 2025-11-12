package com.hortifruti.model;

/**
 * Enum que representa os tipos de movimentação de estoque.
 */
public enum TipoMovimentacao {
    ENTRADA("Entrada"),
    SAIDA("Saída"),
    INICIAL("Inicial");
    
    private final String descricao;
    
    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}