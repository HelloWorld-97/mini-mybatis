package com.danzz.datasource;

import com.danzz.enums.TransactionIsolation;

import java.util.Properties;
import javax.sql.DataSource;

public interface DataSourceFactory {

    void setProperties(Properties prop);

    DataSource getDataSource();

    DataSource getDataSource(boolean defaultAutoCommit, TransactionIsolation transactionIsolation);
}
