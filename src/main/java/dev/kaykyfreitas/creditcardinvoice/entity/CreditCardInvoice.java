package dev.kaykyfreitas.creditcardinvoice.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreditCardInvoice {

    private Customer customer;
    private CreditCard creditCard;
    private List<Transaction> transactions = new ArrayList<>();

    public Double getTotal() {
        return transactions
                .stream()
                .mapToDouble(Transaction::getValue)
                .reduce(0.0, Double::sum);
    }

}
