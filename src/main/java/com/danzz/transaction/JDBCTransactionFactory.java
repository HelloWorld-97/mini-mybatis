package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Data;

@Data
public class JDBCTransactionFactory implements TransactionFactory {

    private DataSource dataSource;

    @Override
    public Transaction newTransaction() {
        return JDBCTransaction.builder().dataSource(dataSource).build();
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
