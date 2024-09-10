package br.nagualcode.kitchenorders.dto;

import java.math.BigDecimal;

public class OrderDTO {

    private Long id;
    private Long customerId;
    private String status;
    private BigDecimal valor;

    public OrderDTO() {
        // Construtor padrão
    }

    public OrderDTO(Long id, Long customerId, String status, BigDecimal valor) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.valor = valor;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
