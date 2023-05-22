package com.danzz.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PooledConnection implements InvocationHandler {

    private Connection realConn;

    private Connection proxyConnection;

    private PooledDataSource dataSource;

    private long createTime;

    private long lastUseTimestamp;

    private long checkoutTimestamp;

    private boolean valid;

    private static final String CLOSE_METHOD_NAME = "close";

    public PooledConnection(PooledDataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.valid = true;
        this.realConn = connection;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{Connection.class}, this);
        this.lastUseTimestamp = System.currentTimeMillis();
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (CLOSE_METHOD_NAME.equals(method.getName())) {
            // 重写close方法
            dataSource.pushConnection(this);
            return null;
        } else {
            return method.invoke(realConn, args);
        }
    }

    public boolean isValid() throws SQLException {
        return valid && this.realConn != null && this.realConn.isClosed();
    }

    public void invalidate() {
        this.valid = false;
    }

    public long getRequestTime() {
        return System.currentTimeMillis() - lastUseTimestamp;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimestamp;
    }
}
