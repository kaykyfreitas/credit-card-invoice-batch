package dev.kaykyfreitas.creditcardinvoice.reader;

import dev.kaykyfreitas.creditcardinvoice.entity.CreditCard;
import dev.kaykyfreitas.creditcardinvoice.entity.Customer;
import dev.kaykyfreitas.creditcardinvoice.entity.Transaction;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class TransactionsReaderConfig {

    @Bean
    public JdbcCursorItemReader<Transaction> transactionsReader(
            @Qualifier("appDataSource")DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .name("transactionsReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM transaction JOIN credit_card USING (credit_card_number) ORDER BY credit_card_number")
                .rowMapper(rowMapperTransaction())
                .build();
    }

    private RowMapper<Transaction> rowMapperTransaction() {
        return new RowMapper<Transaction>() {
            @Override
            public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
                CreditCard creditCard = new CreditCard();
                creditCard.setCreditCardNumber(rs.getInt("credit_card_number"));
                Customer customer = new Customer();
                customer.setId(rs.getInt("customer"));
                creditCard.setCustomer(customer);
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setCreditCard(creditCard);
                transaction.setDate(rs.getDate("date"));
                transaction.setValue(rs.getDouble("value"));
                transaction.setDescription(rs.getString("description"));
                return transaction;
            }
        };
    }

}
