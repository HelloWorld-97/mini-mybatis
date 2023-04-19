package com.danzz.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;

public class MapperProxyFactory<T> {

    // 被代理接口
    Class<T> proxyInterface;

    public MapperProxyFactory(Class<T> t) {
        this.proxyInterface = t;
    }

    public T getInstance(Map<String, String> sqlSession) {
        MapperProxy<T> handler = new MapperProxy<T>(sqlSession, proxyInterface);
        return (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(),
                new Class[]{proxyInterface}, handler);
    }
}
