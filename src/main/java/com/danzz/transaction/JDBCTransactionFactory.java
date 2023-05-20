package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Data;

@Data
public class JDBCTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JDBCTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolation transactionIsolation,
            boolean autoCommit) throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(autoCommit);
        conn.setTransactionIsolation(transactionIsolation.getLevel());
        return JDBCTransaction.builder()
                .dataSource(dataSource)
                .connection(conn).build();
    }
}
