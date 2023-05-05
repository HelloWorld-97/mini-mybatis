package com.danzz.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.Properties;
import javax.sql.DataSource;

public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties prop;

    @Override
    public void setProperties(Properties prop) {
        this.prop = prop;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(prop.getProperty("driver"));
        druidDataSource.setUrl(prop.getProperty("url"));
        druidDataSource.setUsername(prop.getProperty("username"));
        druidDataSource.setPassword(prop.getProperty("password"));
        return druidDataSource;
    }
}
