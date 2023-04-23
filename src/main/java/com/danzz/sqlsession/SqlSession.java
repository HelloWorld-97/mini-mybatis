package com.danzz.sqlsession;

import com.danzz.proxy.MapperProxyFactory;
import com.danzz.registry.MapperRegistry;
import java.util.Map;
import lombok.Data;

@Data
public class SqlSession<T> {

    private Map<String, String> method2Sql;

    private MapperRegistry mapperRegistry;

    public SqlSession(Map<String, String> method2Sql, MapperRegistry mapperRegistry) {
        this.method2Sql = method2Sql;
        this.mapperRegistry = mapperRegistry;
    }

    public T getMapper(Class<T> t) {
        MapperProxyFactory<?> mapperProxyFactory = mapperRegistry.getMapper(t);
        return (T) mapperProxyFactory.getInstance(method2Sql);
    }
}
