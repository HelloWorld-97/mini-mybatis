package com.danzz.config;

import com.danzz.datasource.DruidDataSourceFactory;
import com.danzz.environment.Environment;
import com.danzz.registry.MapperRegistry;
import com.danzz.transaction.JDBCTransactionFactory;
import com.danzz.typealias.TypeAliasRegister;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    Mapper mapper;

    //需要mapperRegistry的能力
    MapperRegistry mapperRegistry;

    Environment environment;

    public Configuration(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
        TypeAliasRegister.typeAliasRegistry("JDBC", JDBCTransactionFactory.class);
        TypeAliasRegister.typeAliasRegistry("druid", DruidDataSourceFactory.class);
    }

    public void addMapper(Class cls) {
        mapperRegistry.addMapper(cls);
    }
}
