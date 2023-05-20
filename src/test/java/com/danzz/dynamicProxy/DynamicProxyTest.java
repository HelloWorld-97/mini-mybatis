package com.danzz.dynamicProxy;

import com.danzz.dao.UserMapper;
import com.danzz.entity.User;
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
                User user = new User(1L, "jack");
                return user;
            }
        };
        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(UserMapper.class.getClassLoader(),
                new Class[]{UserMapper.class},
                handler);
        User user = userMapper.queryUserById(1L);
        System.out.println(user);
    }
}
