package com.danzz.sqlsession;

import com.danzz.config.Configuration;
import com.danzz.enums.TransactionIsolation;
import com.danzz.executor.Executor;
import com.danzz.executor.SimpleExecutor;
import com.danzz.proxy.MapperProxyFactory;
import com.danzz.transaction.Transaction;
import lombok.Data;

import java.sql.SQLException;

@Data
public class SqlSession<T> {

    private Configuration configuration;

    public SqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public T getMapper(Class<T> t) {
        MapperProxyFactory<?> mapperProxyFactory = configuration.getMapperRegistry().getMapper(t);
        return (T) mapperProxyFactory.getInstance(configuration,this);
    }

    public T selectOne(String id,Object[] args) throws Exception {
        Executor executor = configuration.newExecutor(id);
        return (T) executor.query(args).get(0);
    }
}
