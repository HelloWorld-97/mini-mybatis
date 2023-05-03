package com.danzz.proxy;

import com.danzz.config.Configuration;
import com.danzz.config.SelectStatement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler {

    // 关联的sql
    Configuration configuration;

    // 被代理接口
    Class<T> proxyInterface;

    public MapperProxy(Configuration configuration, Class<T> proxyInterface) {
        this.configuration = configuration;
        this.proxyInterface = proxyInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(args);
        } else {
            Map<String, SelectStatement> sqlSession = configuration.getMapper().getMethod2sql();
            System.out.printf("%s 被代理了，执行的sql：%s \r\n", method.getName(),
                    sqlSession.get(method.getName()).getSqlStatement());
            return null;
        }
    }
}
