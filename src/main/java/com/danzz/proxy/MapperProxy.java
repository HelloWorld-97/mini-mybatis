package com.danzz.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler {

    // 关联的sql
    Map<String, String> sqlSession;

    // 被代理接口
    Class<T> proxyInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> proxyInterface) {
        this.sqlSession = sqlSession;
        this.proxyInterface = proxyInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(args);
        } else {
            System.out.printf("%s 被代理了，执行的sql：%s \r\n", method.getName(),
                    sqlSession.get(proxyInterface.getName() + "." + method.getName()));
            return null;
        }
    }
}
