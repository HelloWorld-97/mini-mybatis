package com.danzz.datasource.pooled;

import com.danzz.datasource.unpooled.UnpooledDataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PooledDataSource implements DataSource {

    private PoolState state;

    private UnpooledDataSource dataSource;

    private int maxIdleConnNums = 5;//空闲池最大数量

    private int maxActiveConnNums = 10;//活跃池最大数量

    private long maxCheckoutTime = 30000;//一个连接最长被占用的时间

    private long maxWaitTime = 10000;//最大等待时间

    public PooledDataSource(UnpooledDataSource dataSource, PoolState state) {
        this.dataSource = dataSource;
        this.state = state;
    }


    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state) {
            if (connection.isValid()) {
                state.getActiveQueue().remove(connection);
                state.setRequestCnt(state.getRequestCnt() + 1);
                state.setTotalRequestTime(state.getTotalRequestTime() + connection.getRequestTime());
                if (!state.getIdleQueue().isEmpty() && state.getIdleQueue().size() < maxIdleConnNums) {
                    // 如果空闲池还没满，则创建一个放入空闲池，realConn复用
                    PooledConnection newConn = new PooledConnection(this, connection.getRealConn());
                    state.getIdleQueue().add(newConn);
                    connection.invalidate();
                } else {
                    connection.getRealConn().close();
                    connection.invalidate();
                }
                // 回滚还尚未提交的事务
                if (!connection.getRealConn().getAutoCommit()) {
                    connection.getRealConn().rollback();
                }
                // 唤醒其他线程抢夺连接
                state.notifyAll();
            } else {
                log.error("push invalid connection");
            }
        }
    }

    public PooledConnection popConnection() throws SQLException {
        PooledConnection conn = null;
        synchronized (state) {
            while (conn == null) {
                //先从空闲池中取
                if (!state.getIdleQueue().isEmpty()) {
                    conn = state.getIdleQueue().remove(0);
                    conn.setCheckoutTimestamp(System.currentTimeMillis());
                    conn.setLastUseTimestamp(System.currentTimeMillis());
                    if (conn.isValid()) {
                        // 回滚掉之前的连接池的事务
                        if (!conn.getRealConn().getAutoCommit()) {
                            conn.getRealConn().rollback();
                        }
                        state.getActiveQueue().add(conn);
                    } else {
                        conn.getRealConn().close();
                        conn.invalidate();
                    }
                } else {
                    if (state.getActiveQueue().size() < maxActiveConnNums) {
                        //如果还没达到活跃连接池的最大限制，新建一个连接放入
                        conn = new PooledConnection(this, dataSource.getConnection());
                        state.getActiveQueue().add(conn);
                    } else {
                        if (state.getActiveQueue().get(0).getCheckoutTime() > maxCheckoutTime) {
                            // 如果一个连接被占用的时间超过最长占用时间，直接回滚事务
                            PooledConnection oldestConn = state.getActiveQueue().remove(0);
                            if (!oldestConn.getRealConn().getAutoCommit()) {
                                oldestConn.getRealConn().rollback();
                            }
                            if (oldestConn.isValid()) {
                                oldestConn.invalidate();
                                conn = new PooledConnection(this, oldestConn.getRealConn());
                                state.getActiveQueue().add(conn);
                            } else {
                                oldestConn.getRealConn().close();
                                oldestConn.invalidate();
                                conn = new PooledConnection(this, dataSource.getConnection());
                                state.getActiveQueue().add(conn);
                            }
                        } else {
                            // 活跃连接池已经满了，等待
                            try {
                                state.setWaitCnt(state.getWaitCnt() + 1);
                                long waitStartTime = System.currentTimeMillis();
                                state.wait(maxWaitTime);
                                state.setTotalWaitTime(
                                        state.getTotalWaitTime() + System.currentTimeMillis() - waitStartTime);
                            } catch (InterruptedException e) {
                                log.error("获取数据库连接失败");
                                throw new SQLException("获取数据库连接失败");
                            }
                        }
                    }
                }
                if (conn != null) {
                    conn.setLastUseTimestamp(System.currentTimeMillis());
                    conn.setCheckoutTimestamp(System.currentTimeMillis());
                    return conn;
                }
            }
        }
        return null;
    }

    public boolean pingConnection(PooledConnection connection) {
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(dataSource.getUsername(), dataSource.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection().getProxyConnection();
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
