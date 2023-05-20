package com.danzz.transaction;

import com.danzz.enums.TransactionIsolation;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class JDBCTransaction implements Transaction {

    private DataSource dataSource;
    private Connection connection;

    public JDBCTransaction (Connection connection){
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // 一个tx复用一个connection
        if (connection != null) {
            return connection;
        }
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(TransactionIsolation txIsolation, boolean autoCommit) throws SQLException {
        // 获取一个自定义的connection
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(autoCommit);
        connection.setTransactionIsolation(txIsolation.getLevel());
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null) {
            connection.commit();
        }else{
            throw new SQLException("connection is null");
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }else {
            throw new SQLException("connection is null");
        }
    }

    @Override
    public void close(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }else {
            throw new SQLException("connection is null");
        }
    }
}
