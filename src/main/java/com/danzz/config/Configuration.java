package com.danzz.config;

import com.danzz.datasource.DruidDataSourceFactory;
import com.danzz.datasource.pooled.PooledDataSourceFactory;
import com.danzz.datasource.unpooled.UnpooledDataSourceFactory;
import com.danzz.environment.Environment;
import com.danzz.registry.MapperRegistry;
import com.danzz.transaction.JDBCTransactionFactory;
import com.danzz.typealias.TypeAliasRegister;
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
}
