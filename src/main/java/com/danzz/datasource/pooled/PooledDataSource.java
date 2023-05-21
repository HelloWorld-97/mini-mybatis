package com.danzz.datasource.pooled;

import com.danzz.datasource.unpooled.UnpooledDataSource;
import lombok.Data;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Data
public class PooledDataSource implements DataSource {

    private PoolState state;

    private UnpooledDataSource dataSource;

    private int maxIdleConnNums = 5;//空闲池最大数量

    private int maxActiveConnNums = 10;//活跃池最大数量


    public void pushConnection(PooledConnection connection){

    }

    public PooledConnection popConnection(){
        return null;
    }

    public boolean pingConnection(PooledConnection connection){
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
