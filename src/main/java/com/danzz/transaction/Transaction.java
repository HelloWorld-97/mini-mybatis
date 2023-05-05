package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;
import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {

    Connection getConnection() throws SQLException;

    Connection getConnection(TransactionIsolation txIsolation, boolean autoCommit) throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close(Connection connection) throws SQLException;
}
