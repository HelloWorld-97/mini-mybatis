package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;
import java.sql.SQLException;
import javax.sql.DataSource;

public interface TransactionFactory {

    Transaction newTransaction();

    Transaction newTransaction(DataSource dataSource, TransactionIsolation transactionIsolation, boolean autoCommit)
            throws SQLException;
}
