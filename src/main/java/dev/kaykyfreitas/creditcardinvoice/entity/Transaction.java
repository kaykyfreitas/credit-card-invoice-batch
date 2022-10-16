package dev.kaykyfreitas.creditcardinvoice.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Transaction {

    private Integer id;
    private CreditCard creditCard;
    private String description;
    private Double value;
    private Date date;

}
