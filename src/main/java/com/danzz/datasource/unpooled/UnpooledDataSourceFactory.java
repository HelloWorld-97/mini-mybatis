package com.danzz.datasource.unpooled;

import com.alibaba.druid.pool.DruidDataSource;
import com.danzz.datasource.DataSourceFactory;
import com.danzz.enums.TransactionIsolation;

import javax.sql.DataSource;
import java.util.Properties;

public class UnpooledDataSourceFactory implements DataSourceFactory {
    private Properties prop;

    @Override
    public void setProperties(Properties prop) {
        this.prop = prop;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(prop.getProperty("driver"));
        unpooledDataSource.setUrl(prop.getProperty("url"));
        unpooledDataSource.setUsername(prop.getProperty("username"));
        unpooledDataSource.setPassword(prop.getProperty("password"));
        return unpooledDataSource;
    }

    @Override
    public DataSource getDataSource(boolean defaultAutoCommit, TransactionIsolation transactionIsolation) {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(prop.getProperty("driver"));
        unpooledDataSource.setUrl(prop.getProperty("url"));
        unpooledDataSource.setUsername(prop.getProperty("username"));
        unpooledDataSource.setPassword(prop.getProperty("password"));
        unpooledDataSource.setAutoCommit(defaultAutoCommit);
        unpooledDataSource.setTransactionIsolation(transactionIsolation.getLevel());
        return unpooledDataSource;
    }
}
