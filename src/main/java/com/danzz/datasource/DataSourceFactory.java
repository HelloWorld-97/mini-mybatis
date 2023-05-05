package com.danzz.datasource;

import java.util.Properties;
import javax.sql.DataSource;

public interface DataSourceFactory {

    void setProperties(Properties prop);

    DataSource getDataSource();
}
