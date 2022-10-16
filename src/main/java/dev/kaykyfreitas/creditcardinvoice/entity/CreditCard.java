package dev.kaykyfreitas.creditcardinvoice.entity;

import lombok.Data;

@Data
public class CreditCard {

    private Integer creditCardNumber;
    private Customer customer;

}
