package dev.kaykyfreitas.creditcardinvoice.job;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CreditCardInvoiceJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job creditCardInvoiceJob(Step creditCardInvoiceStep) {
        return jobBuilderFactory
                .get("creditCardInvoiceStep")
                .start(creditCardInvoiceStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
