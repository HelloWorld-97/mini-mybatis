package com.danzz.datasource.pooled;

import com.danzz.datasource.DataSourceFactory;
import com.danzz.datasource.unpooled.UnpooledDataSource;
import com.danzz.enums.TransactionIsolation;

import javax.sql.DataSource;
import java.util.Properties;

public class PooledDataSourceFactory implements DataSourceFactory {
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
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDataSource(unpooledDataSource);
        return pooledDataSource;
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
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDataSource(unpooledDataSource);
        return pooledDataSource;
    }
}
