package com.danzz.proxy;

import com.danzz.config.Configuration;
import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {

    // 被代理接口
    Class<T> proxyInterface;

    public MapperProxyFactory(Class<T> t) {
        this.proxyInterface = t;
    }

    public T getInstance(Configuration configuration) {
        MapperProxy<T> handler = new MapperProxy<T>(configuration, proxyInterface);
        return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(),
                new Class[]{proxyInterface}, handler);
    }
}
