package com.danzz.proxyfactory;

import com.danzz.dao.UserQueryDao;
import com.danzz.entity.User;
import com.danzz.proxy.MapperProxyFactory;
import com.danzz.registry.MapperRegistry;
import com.danzz.sqlsession.SqlSession;
import java.util.HashMap;
import org.junit.Test;

public class ProxyFactoryTest {

    @Test
    public void testProxyFactory() {
        MapperProxyFactory<UserQueryDao> proxyFactory = new MapperProxyFactory<UserQueryDao>(UserQueryDao.class);
        // 待关联的sql
        HashMap<String, String> sqlSession = new HashMap<String, String>() {{
            put("com.danzz.dao.UserQueryDao.deleteUserById", "delete from t_user where id = 1");
        }};
        UserQueryDao instance = proxyFactory.getInstance(sqlSession);
        instance.deleteUserById("1");
        instance.queryUserById("1");
    }

    //Day2 测试mapperRegistry功能
    @Test
    public void testMapperRegistry() {
        MapperRegistry mapperRegistry = new MapperRegistry("com.danzz.dao");
        // 扫描mapper
        mapperRegistry.scanPackage();
        // 模拟代理sql
        HashMap<String, String> sqlMap = new HashMap<String, String>() {{
            put("com.danzz.dao.UserInsertDao.saveUser", "insert into t_user (\"1\",\"jack\")");
            put("com.danzz.dao.UserQueryDao.queryUserById", "queryUserById,res:{id:1,name:jack}");
        }};
        SqlSession<UserQueryDao> sqlSession = new SqlSession<>(sqlMap, mapperRegistry);
        UserQueryDao mapper = sqlSession.getMapper(UserQueryDao.class);
        User user = mapper.queryUserById("1");
    }
}
