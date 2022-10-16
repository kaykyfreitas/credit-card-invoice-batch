package dev.kaykyfreitas.creditcardinvoice.processor;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCardInvoice;
import dev.kaykyfreitas.creditcardinvoice.entity.Customer;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoadCustomerDataProcessor implements ItemProcessor<CreditCardInvoice, CreditCardInvoice> {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public CreditCardInvoice process(CreditCardInvoice creditCardInvoice) throws Exception {
        String uri = String
                .format(
                        "http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d",
                        creditCardInvoice.getCustomer().getId()
                );
        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);

        if(response.getStatusCode() != HttpStatus.OK)
            throw new ValidationException("Customer not found");

        creditCardInvoice.setCustomer(response.getBody());

        return creditCardInvoice;
    }

}
