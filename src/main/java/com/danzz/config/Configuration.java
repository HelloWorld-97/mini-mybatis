package com.danzz.config;

import com.danzz.datasource.DruidDataSourceFactory;
import com.danzz.datasource.pooled.PooledDataSourceFactory;
import com.danzz.datasource.unpooled.UnpooledDataSourceFactory;
import com.danzz.enums.TransactionIsolation;
import com.danzz.environment.Environment;
import com.danzz.executor.Executor;
import com.danzz.executor.SimpleExecutor;
import com.danzz.executor.result.SimpleResultHandler;
import com.danzz.executor.statement.PreparedStatementHandler;
import com.danzz.executor.statement.StatementHandler;
import com.danzz.registry.MapperRegistry;
import com.danzz.transaction.JDBCTransactionFactory;
import com.danzz.transaction.Transaction;
import com.danzz.typealias.TypeAliasRegister;

import java.sql.SQLException;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    private HashMap<String, Mapper> ns2mapper;

    //需要mapperRegistry的能力
    MapperRegistry mapperRegistry;

    Environment environment;

    public Configuration(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.ns2mapper = new HashMap<>();
        TypeAliasRegister.typeAliasRegistry("JDBC", JDBCTransactionFactory.class);
        TypeAliasRegister.typeAliasRegistry("druid", DruidDataSourceFactory.class);
        TypeAliasRegister.typeAliasRegistry("unpooled", UnpooledDataSourceFactory.class);
        TypeAliasRegister.typeAliasRegistry("pooled", PooledDataSourceFactory.class);
    }

    public void addMapper(Class cls) {
        mapperRegistry.addMapper(cls);
    }

    public void addMapper(Mapper mapper) {
        ns2mapper.putIfAbsent(mapper.getNamespace(), mapper);
    }

    public Mapper getMapper(Class cls) {
        if (!ns2mapper.containsKey(cls.getName())) {
            log.error("mapper not exist");
        }
        return ns2mapper.get(cls.getName());
    }

    public Executor newExecutor(String id) throws SQLException {
        Transaction transaction = this.getEnvironment().getTransactionFactory().newTransaction(this.getEnvironment().getDataSource(), TransactionIsolation.READ_COMMITTED, true);
        String[] split = id.split("#");
        Mapper mapper = ns2mapper.get(split[0]);
        SelectStatement selectStatement = mapper.getMethod2sql().get(split[1]);
        StatementHandler statementHandler = newPreparedStatementHandler(mapper, selectStatement);
        SimpleResultHandler simpleResultHandler = newSimpleResultHandler();
        SimpleExecutor simpleExecutor = new SimpleExecutor(this, transaction,statementHandler,simpleResultHandler);
        return simpleExecutor;
    }

    public StatementHandler newPreparedStatementHandler(Mapper mapper, SelectStatement selectStatement){
        return new PreparedStatementHandler(mapper,selectStatement);
    }

    public SimpleResultHandler newSimpleResultHandler(){
        return new SimpleResultHandler();
    }
}
