package br.nagualcode.kitchenorders.dto;

import java.util.List;

public class CustomerDTO {

    private Long id;
    private String nome;
    private String email;
    private List<Long> orders;

    // Constructor
    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String nome, String email, List<Long> orders) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.orders = orders;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getOrders() {
        return orders;
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders;
    }
}
