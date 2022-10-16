package dev.kaykyfreitas.creditcardinvoice.writer;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCardInvoice;
import dev.kaykyfreitas.creditcardinvoice.entity.Transaction;

import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

@Configuration
public class CreditCardInvoiceFileWriterConfig {

    @Bean
    public MultiResourceItemWriter<CreditCardInvoice> creditCardInvoiceFilesWriter() {
        return new MultiResourceItemWriterBuilder<CreditCardInvoice>()
                .name("creditCardInvoiceFilesWriter")
                .resource(new FileSystemResource("files/invoice"))
                .itemCountLimitPerResource(1)
                .resourceSuffixCreator(suffixCreator())
                .delegate(creditCardInvoiceFile())
                .build();
    }

    private FlatFileItemWriter<CreditCardInvoice> creditCardInvoiceFile() {
        return new FlatFileItemWriterBuilder<CreditCardInvoice>()
                .name("creditCardInvoiceFile")
                .resource(new FileSystemResource("files/invoice.txt"))
                .lineAggregator(lineAggregator())
                .headerCallback(headerCallback())
                .footerCallback(footerCallback())
                .build();
    }

    @Bean
    public FlatFileFooterCallback footerCallback() {
        return new TotalTransactionFooterCallback();
    }

    private FlatFileHeaderCallback headerCallback() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.append(String.format("%121s\n", "Future Bank"));
                writer.append(String.format("%121s\n\n", "Awesome Street, 131"));
            }
        };
    }

    private LineAggregator<CreditCardInvoice> lineAggregator() {
        return new LineAggregator<CreditCardInvoice>() {
            @Override
            public String aggregate(CreditCardInvoice creditCardInvoice) {
                StringBuilder writer = new StringBuilder();
                writer.append(String.format("Name: %s\n", creditCardInvoice.getCustomer().getName()));
                writer.append(String.format("Address: %s\n\n\n", creditCardInvoice.getCustomer().getAddress()));
                writer.append(String.format("Full invoice of card %d\n", creditCardInvoice.getCreditCard().getCreditCardNumber()));
                writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
                writer.append("DATE DESCRIPTION VALUE\n");
                writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
                for(Transaction transaction : creditCardInvoice.getTransactions()) {
                    writer.append(String.format("\n[%10s] %-80s - %s",
                            new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate()),
                            transaction.getDescription(),
                            NumberFormat.getCurrencyInstance().format(transaction.getValue()))
                    );
                }
                return writer.toString();
            }
        };
    }

    private ResourceSuffixCreator suffixCreator() {
        return new ResourceSuffixCreator() {
            @Override
            public String getSuffix(int index) {
                return index + ".txt" ;
            }
        };
    }

}
