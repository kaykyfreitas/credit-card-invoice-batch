package dev.kaykyfreitas.creditcardinvoice.entity;

import lombok.Data;

@Data
public class Customer {

    private Integer id;
    private String name;
    private String address;


    // This setters are used for receive portuguese data and map to this english class
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public void setEndereco(String endereco) {
        this.address = endereco;
    }

}
