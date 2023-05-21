package com.danzz.datasource.unpooled;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

@Slf4j
@Data
public class UnpooledDataSource implements DataSource {

    private String url;

    private String driver;

    private String username;

    private String password;

    // 使用包装类型来接避免默认值
    private Boolean autoCommit;

    private Integer transactionIsolation;

    private static Map<String,Driver> registerDriver;

    static {
        registerDriver = new HashMap<>();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            registerDriver.put(driver.getClass().getName(),driver);
        }
    }

    private synchronized void registerDriver(String driver) throws Exception {
        if (!registerDriver.containsKey(driver)){
            Class<?> driverCls = Class.forName(driver);
            Driver driverIns = (Driver)driverCls.newInstance();
            DriverManager.registerDriver(driverIns);
            registerDriver.put(driver,driverIns);
        }
    }

    private Connection doGetConnection(String username,String password) throws Exception {
        registerDriver(driver);
        Properties prop = new Properties();
        prop.put("user",username);
        prop.put("password",password);
        Connection connection = DriverManager.getConnection(url, prop);
        // 赋默认值
        if (autoCommit != null && autoCommit != connection.getAutoCommit()){
            connection.setAutoCommit(autoCommit);
        }
        if (transactionIsolation != null) {
            connection.setTransactionIsolation(transactionIsolation);
        }
        return connection;
    }

    @Override
    public Connection getConnection() {
        try {
            return doGetConnection(this.username,this.password);
        } catch (Exception e) {
            log.error("doGetConnection failed:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            return doGetConnection(username,password);
        } catch (Exception e) {
            log.error("doGetConnection failed:{}",e.getMessage());
            throw new RuntimeException(e);
        }
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
