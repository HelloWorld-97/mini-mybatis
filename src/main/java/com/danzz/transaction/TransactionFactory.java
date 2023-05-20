package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public interface TransactionFactory {

    Transaction newTransaction(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolation transactionIsolation, boolean autoCommit)
            throws SQLException;
}
