package com.hortifruti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object para requisição de movimentação de estoque.
 */
public class MovimentacaoRequest {

    @NotBlank(message = "O tipo de movimentação é obrigatório")
    private String tipo;

    @NotNull(message = "A quantidade é obrigatória")
    private Double quantidade;

    /**
     * Retorna o tipo de movimentação.
     * @return O tipo de movimentação.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo de movimentação.
     * @param tipo O tipo de movimentação.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Retorna a quantidade da movimentação.
     * @return A quantidade da movimentação.
     */
    public Double getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade da movimentação.
     * @param quantidade A quantidade da movimentação.
     */
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
