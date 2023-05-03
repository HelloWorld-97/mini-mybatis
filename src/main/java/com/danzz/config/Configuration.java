package com.danzz.config;

import com.danzz.registry.MapperRegistry;
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

    public Configuration(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    public void addMapper(Class cls) {
        mapperRegistry.addMapper(cls);
    }
}
