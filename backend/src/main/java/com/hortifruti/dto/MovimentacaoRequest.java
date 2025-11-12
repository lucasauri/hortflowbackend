package com.hortifruti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovimentacaoRequest {

    @NotBlank(message = "O tipo de movimentação é obrigatório")
    private String tipo;

    @NotNull(message = "A quantidade é obrigatória")
    private Double quantidade;

    // Getters and Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
