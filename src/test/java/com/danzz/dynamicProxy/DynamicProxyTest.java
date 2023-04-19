package com.danzz.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.Test;

public class DynamicProxyTest {

    @Test
    public void testDynamicProxy() {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                if (method.getName().equals("queryUserById")) {
                    System.out.println("queryUserById,res:{id:1,name:jack}");
                }
                User user = new User("1", "jack");
                return user;
            }
        };
        UserQueryDao userQueryDao = (UserQueryDao) Proxy.newProxyInstance(UserQueryDao.class.getClassLoader(),
                new Class[]{UserQueryDao.class},
                handler);
        User user = userQueryDao.queryUserById("1");
        System.out.println(user);
    }
}
