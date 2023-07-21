package com.danzz.proxy;

import com.danzz.config.Configuration;
import com.danzz.config.Mapper;
import com.danzz.config.SelectStatement;
import com.danzz.enums.TransactionIsolation;
import com.danzz.mapping.MappedMethod;
import com.danzz.sqlsession.SqlSession;
import com.danzz.transaction.Transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapperProxy<T> implements InvocationHandler {

    // 关联的sql
    Configuration configuration;

    // 被代理接口
    Class<T> proxyInterface;

    SqlSession<T> sqlSession;

    // method2MappedMethod
    Map<Method, MappedMethod> cachedMethods = new ConcurrentHashMap<>();

    public MapperProxy(Configuration configuration,SqlSession  sqlSession, Class<T> proxyInterface) {
        this.configuration = configuration;
        this.sqlSession = sqlSession;
        this.proxyInterface = proxyInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(args);
        } else {
//            //1.start transaction
//            Transaction transaction = configuration.getEnvironment().getTransactionFactory().
//                    newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolation.READ_COMMITTED,true  );
//            Connection connection = transaction.getConnection();
//            //2.create preparedStatement
//            SelectStatement selectStatement = configuration.getMapper(method.getDeclaringClass())
//                    .getMethod2sql().get(method.getName());
//            String sql = selectStatement.getSqlStatement();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setLong(1,(Long) args[0]);
//            //3.execute sql
//            ResultSet resultSet = preparedStatement.executeQuery();
//            //4.parse resultSet
//            List<Object> resList = parseResultSet(resultSet, Class.forName(selectStatement.getResultType()));
//            //5.close connection
//            transaction.close(connection);
            MappedMethod mapperMethod = getMapperMethod(method);
            return mapperMethod.execute(sqlSession,args);
        }
    }

    public MappedMethod getMapperMethod(Method method){
        if (cachedMethods.containsKey(method)) {
            return cachedMethods.get(method);
        }
        Mapper mapper = configuration.getNs2mapper().get(method.getDeclaringClass().getName());
        SelectStatement selectStatement = mapper.getMethod2sql().get(method.getName());
        MappedMethod mappedMethod = new MappedMethod(mapper.getNamespace() + "#"+selectStatement.getId(),selectStatement.getCommandType());
        cachedMethods.putIfAbsent(method,mappedMethod);
        return cachedMethods.get(method);
    }

    // 通过反射获取结果集中每个结果的属性并设置值

}
