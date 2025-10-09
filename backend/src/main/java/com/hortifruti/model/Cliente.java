package com.hortifruti.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Classe que representa um cliente no sistema Hortifruti.
 * Agora mapeada como entidade JPA.
 */
@Entity
@Table(name = "clientes")
public class Cliente {
    
    /** Identificador único do cliente */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Nome do cliente */
    @NotBlank(message = "Nome do cliente é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;
    
    /** CPF do cliente */
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    @Column(name = "cpf", unique = true, nullable = false, length = 14)
    private String cpf;
    
    /** Estado do cliente */
    @Column(name = "estado")
    private String estado;
    
    /** Telefone do cliente */
    @Column(name = "telefone")
    private String telefone;
    
    /** CNPJ do cliente */
    @Column(name = "cnpj")
    private String cnpj;
    
    /** Inscrição Estadual do cliente */
    @Column(name = "ie")
    private String ie;
    
    /** Condição de pagamento */
    @Column(name = "cond_pgto")
    private String condPgto;
    
    /** Banco do cliente */
    @Column(name = "banco")
    private String banco;
    
    /**
     * Construtor padrão da classe Cliente.
     */
    public Cliente() {
    }
    
    /**
     * Construtor com parâmetros principais.
     * 
     * @param nome Nome do cliente
     * @param estado Estado do cliente
     * @param telefone Telefone do cliente
     */
    public Cliente(String nome, String estado, String telefone) {
        this.nome = nome;
        this.estado = estado;
        this.telefone = telefone;
    }
    
    /**
     * Construtor completo com todos os parâmetros.
     * 
     * @param id Identificador único
     * @param nome Nome do cliente
     * @param estado Estado do cliente
     * @param telefone Telefone do cliente
     * @param cnpj CNPJ do cliente
     * @param ie Inscrição Estadual
     * @param condPgto Condição de pagamento
     * @param banco Banco do cliente
     */
    public Cliente(Long id, String nome, String estado, String telefone, 
                   String cnpj, String ie, String condPgto, String banco) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.telefone = telefone;
        this.cnpj = cnpj;
        this.ie = ie;
        this.condPgto = condPgto;
        this.banco = banco;
    }
    
    /**
     * Verifica se o cliente possui CNPJ válido.
     * 
     * @return true se possui CNPJ, false caso contrário
     */
    public boolean possuiCnpj() {
        return cnpj != null && !cnpj.trim().isEmpty();
    }
    
    /**
     * Verifica se o cliente possui dados completos.
     * 
     * @return true se possui dados completos, false caso contrário
     */
    public boolean isDadosCompletos() {
        return nome != null && !nome.trim().isEmpty() &&
               estado != null && !estado.trim().isEmpty() &&
               telefone != null && !telefone.trim().isEmpty();
    }
    
    // Getters e Setters
    
    /**
     * Obtém o identificador do cliente.
     * 
     * @return ID do cliente
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador do cliente.
     * 
     * @param id ID do cliente
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome do cliente.
     * 
     * @return Nome do cliente
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do cliente.
     * 
     * @param nome Nome do cliente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém o estado do cliente.
     * 
     * @return Estado do cliente
     */
    public String getEstado() {
        return estado;
    }
    
    /**
     * Define o estado do cliente.
     * 
     * @param estado Estado do cliente
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Obtém o telefone do cliente.
     * 
     * @return Telefone do cliente
     */
    public String getTelefone() {
        return telefone;
    }
    
    /**
     * Define o telefone do cliente.
     * 
     * @param telefone Telefone do cliente
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    /**
     * Obtém o CNPJ do cliente.
     * 
     * @return CNPJ do cliente
     */
    public String getCnpj() {
        return cnpj;
    }
    
    /**
     * Define o CNPJ do cliente.
     * 
     * @param cnpj CNPJ do cliente
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    /**
     * Obtém a Inscrição Estadual do cliente.
     * 
     * @return IE do cliente
     */
    public String getIe() {
        return ie;
    }
    
    /**
     * Define a Inscrição Estadual do cliente.
     * 
     * @param ie IE do cliente
     */
    public void setIe(String ie) {
        this.ie = ie;
    }
    
    /**
     * Obtém a condição de pagamento.
     * 
     * @return Condição de pagamento
     */
    public String getCondPgto() {
        return condPgto;
    }
    
    /**
     * Define a condição de pagamento.
     * 
     * @param condPgto Condição de pagamento
     */
    public void setCondPgto(String condPgto) {
        this.condPgto = condPgto;
    }
    
    /**
     * Obtém o banco do cliente.
     * 
     * @return Banco do cliente
     */
    public String getBanco() {
        return banco;
    }
    
    /**
     * Obtém o CPF do cliente.
     * 
     * @return CPF do cliente
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do cliente.
     * 
     * @param cpf CPF do cliente
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    /**
     * Define o banco do cliente.
     * 
     * @param banco Banco do cliente
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", estado='" + estado + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return id != null && id.equals(cliente.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 