package dev.kaykyfreitas.creditcardinvoice.writer;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCardInvoice;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

public class TotalTransactionFooterCallback implements FlatFileFooterCallback {

    private Double total = 0.0;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write(String.format("\n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));
    }

    @BeforeWrite
    public void beforeWrite(List<CreditCardInvoice> invoices) {
        for (CreditCardInvoice creditCardInvoice : invoices)
            total += creditCardInvoice.getTotal();
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        total = 0.0;
    }

}
