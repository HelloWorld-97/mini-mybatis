package com.danzz.sqlsession;

import com.danzz.config.Configuration;
import com.danzz.proxy.MapperProxyFactory;
import lombok.Data;

@Data
public class SqlSession<T> {

    private Configuration configuration;

    public SqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public T getMapper(Class<T> t) {
        MapperProxyFactory<?> mapperProxyFactory = configuration.getMapperRegistry().getMapper(t);
        return (T) mapperProxyFactory.getInstance(configuration);
    }
}
