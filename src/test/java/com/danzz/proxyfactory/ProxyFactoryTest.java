package com.danzz.proxyfactory;

import com.danzz.dao.UserMapper;
import com.danzz.datasource.pooled.PooledDataSource;
import com.danzz.entity.User;
import com.danzz.sqlsession.SqlSession;
import com.danzz.sqlsession.SqlSessionFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ProxyFactoryTest {

    static ThreadPoolExecutor tpe = new ThreadPoolExecutor(1000, 1000, 0, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>());
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
        SqlSessionFactory<UserMapper> sqlSessionFactory = new SqlSessionFactory<UserMapper>()
                .fileName("db-config.xml")
                .build();
        SqlSession<UserMapper> sqlSession = sqlSessionFactory.openSession();
        // 获取mapper并查询
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.queryUserById(1L);
        log.info("user:{}", user);
    }

    @Test
    public void testPooledDataSource() throws InterruptedException {
        SqlSessionFactory<UserMapper> sqlSessionFactory = new SqlSessionFactory<UserMapper>()
                .fileName("db-config.xml")
                .build();
        SqlSession<UserMapper> sqlSession = sqlSessionFactory.openSession();
        // 获取mapper并查询
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        // 获取dataSource
        PooledDataSource dataSource = (PooledDataSource) sqlSession.getConfiguration().getEnvironment().getDataSource();
        // 启动打印线程
        Thread printer = new Thread(() -> {
            while (true) {
                log.info("reqCnt:{},totalTime:{},waitCnt:{},waitTime:{},checkoutCnt:{},checkoutTime:{}",
                        dataSource.getState().getRequestCnt(), dataSource.getState().getTotalRequestTime() / 1000,
                        dataSource.getState().getWaitCnt(), dataSource.getState().getTotalWaitTime() / 1000,
                        dataSource.getState().getCheckoutCnt(), dataSource.getState().getTotalCheckoutTime() / 1000);
                log.info("idleConnSize:{},activeConnSize:{}", dataSource.getState().getIdleQueue().size(),
                        dataSource.getState().getActiveQueue().size());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        printer.start();
        // 并行执行
        int times = 100;
        int periodThreadNums = 10;
        CountDownLatch cdl = new CountDownLatch(times * periodThreadNums);
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < periodThreadNums; j++) {
                tpe.execute(() -> {
                    try {
                        mapper.queryUserById(1L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        cdl.countDown();
                    }
                });
            }
            Thread.sleep(1000);
        }
        cdl.await();
        tpe.shutdown();

        Thread.sleep(10000);
    }


}
