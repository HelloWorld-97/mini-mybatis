package com.danzz.registry;

import cn.hutool.core.lang.ClassScanner;
import com.danzz.proxy.MapperProxyFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class MapperRegistry {

    private String basePackagePath;
    private Map<Class<?>, MapperProxyFactory<?>> mapper2MapperProxy;

    public MapperRegistry(String basePackagePath) {
        this.basePackagePath = basePackagePath;
        this.mapper2MapperProxy = new HashMap<>();
    }

    public MapperProxyFactory<?> getMapper(Class<?> mapper) {
        return mapper2MapperProxy.get(mapper);
    }

    private void addMapper(Class<?> t) {
        if (!mapper2MapperProxy.containsKey(t)) {
            mapper2MapperProxy.put(t, new MapperProxyFactory<>(t));
        }
    }

    public void scanPackage() {
        Set<Class<?>> daos = ClassScanner.scanPackage(basePackagePath);
        for (Class<?> dao : daos) {
            addMapper(dao);
        }
    }
}
