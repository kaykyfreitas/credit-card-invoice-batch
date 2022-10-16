package dev.kaykyfreitas.creditcardinvoice.step;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCardInvoice;

import dev.kaykyfreitas.creditcardinvoice.entity.Transaction;
import dev.kaykyfreitas.creditcardinvoice.reader.CreditCardInvoiceReader;
import dev.kaykyfreitas.creditcardinvoice.writer.TotalTransactionFooterCallback;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CreditCardInvoiceStepConfig {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step creditCardInvoiceStep(
            ItemStreamReader<Transaction> transactionsReader,
            ItemProcessor<CreditCardInvoice, CreditCardInvoice> loadCustomerDataProcessor,
            ItemWriter<CreditCardInvoice> creditCardInvoiceWriter,
            TotalTransactionFooterCallback footerCallback) {
        return stepBuilderFactory
                .get("creditCardInvoiceStep")
                .<CreditCardInvoice, CreditCardInvoice>chunk(1)
                .reader(new CreditCardInvoiceReader(transactionsReader))
                .processor(loadCustomerDataProcessor)
                .writer(creditCardInvoiceWriter)
                .listener(footerCallback)
                .build();
    }

}
