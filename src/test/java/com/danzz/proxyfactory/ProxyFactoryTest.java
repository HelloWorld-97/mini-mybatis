package com.danzz.proxyfactory;

import com.danzz.dynamicProxy.UserQueryDao;
import com.danzz.proxy.MapperProxyFactory;
import java.util.HashMap;
import org.junit.Test;

public class ProxyFactoryTest {

    @Test
    public void testProxyFactory() {
        MapperProxyFactory<UserQueryDao> proxyFactory = new MapperProxyFactory<UserQueryDao>(UserQueryDao.class);
        // 待关联的sql
        HashMap<String, String> sqlSession = new HashMap<String, String>() {{
            put("com.danzz.dynamicProxy.UserQueryDao.deleteUserById", "delete from t_user where id = 1");
        }};
        UserQueryDao instance = proxyFactory.getInstance(sqlSession);
        instance.deleteUserById("1");
        instance.queryUserById("1");
    }
}
