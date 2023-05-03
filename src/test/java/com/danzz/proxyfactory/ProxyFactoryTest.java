package com.danzz.proxyfactory;

import com.danzz.dao.UserQueryDao;
import com.danzz.sqlsession.SqlSession;
import com.danzz.sqlsession.SqlSessionFactory;
import org.junit.Test;

public class ProxyFactoryTest {

    // day1 动态代理
//    @Test
//    public void testProxyFactory() {
//        MapperProxyFactory<UserQueryDao> proxyFactory = new MapperProxyFactory<UserQueryDao>(UserQueryDao.class);
//        // 待关联的sql
//        HashMap<String, String> sqlSession = new HashMap<String, String>() {{
//            put("com.danzz.dao.UserQueryDao.deleteUserById", "delete from t_user where id = 1");
//        }};
//        UserQueryDao instance = proxyFactory.getInstance(sqlSession);
//        instance.deleteUserById("1");
//        instance.queryUserById("1");
//    }

    //Day2 测试mapperRegistry功能
//    @Test
//    public void testMapperRegistry() {
//        MapperRegistry mapperRegistry = new MapperRegistry("com.danzz.dao");
//        // 扫描mapper
//        mapperRegistry.scanPackage();
//        // 模拟代理sql
//        HashMap<String, String> sqlMap = new HashMap<String, String>() {{
//            put("com.danzz.dao.UserInsertDao.saveUser", "insert into t_user (\"1\",\"jack\")");
//            put("com.danzz.dao.UserQueryDao.queryUserById", "queryUserById,res:{id:1,name:jack}");
//        }};
//        SqlSession<UserQueryDao> sqlSession = new SqlSession<>(sqlMap, mapperRegistry);
//        UserQueryDao mapper = sqlSession.getMapper(UserQueryDao.class);
//        User user = mapper.queryUserById("1");
//    }

    //day03 测试xml解析自动注册功能
    @Test
    public void testXmlParser() {
        SqlSessionFactory<UserQueryDao> sqlSessionFactory = new SqlSessionFactory<UserQueryDao>()
                .fileName("UserQueryDao.xml")
                .build();
        SqlSession<UserQueryDao> sqlSession = sqlSessionFactory.openSession();
        // 获取mapper并查询
        UserQueryDao mapper = sqlSession.getMapper(UserQueryDao.class);
        mapper.queryUserById("1");
    }
}
