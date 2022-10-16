package dev.kaykyfreitas.creditcardinvoice.reader;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCardInvoice;
import dev.kaykyfreitas.creditcardinvoice.entity.Transaction;

import org.springframework.batch.item.*;

import java.util.Objects;

public class CreditCardInvoiceReader implements ItemStreamReader<CreditCardInvoice> {

    private ItemStreamReader<Transaction> delegate;
    private Transaction actualTransaction;

    public CreditCardInvoiceReader(ItemStreamReader<Transaction> delegate) {
        this.delegate = delegate;
    }

    @Override
    public CreditCardInvoice read() throws Exception {
        if(actualTransaction == null)
            actualTransaction = delegate.read();

        CreditCardInvoice creditCardInvoice = null;
        Transaction transaction = actualTransaction;
        actualTransaction = null;

        if(transaction != null) {
            creditCardInvoice = new CreditCardInvoice();
            creditCardInvoice.setCreditCard(transaction.getCreditCard());
            creditCardInvoice.setCustomer(transaction.getCreditCard().getCustomer());
            creditCardInvoice.getTransactions().add(transaction);
            while(isRelatedTransaction(transaction))
                creditCardInvoice.getTransactions().add(actualTransaction);
        }
        return creditCardInvoice;
    }

    private boolean isRelatedTransaction(Transaction transaction) throws Exception {
        return peek() != null && Objects.equals(transaction.getCreditCard().getCreditCardNumber(), actualTransaction.getCreditCard().getCreditCardNumber());
    }

    private Transaction peek() throws Exception {
        actualTransaction = delegate.read();
        return actualTransaction;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

}
