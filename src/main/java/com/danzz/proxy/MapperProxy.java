package com.danzz.proxy;

import com.danzz.config.Configuration;
import com.danzz.config.SelectStatement;
import com.danzz.enums.TransactionIsolation;
import com.danzz.transaction.Transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
            //1.start transaction
            Transaction transaction = configuration.getEnvironment().getTransactionFactory().
                    newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolation.READ_COMMITTED,true  );
            Connection connection = transaction.getConnection();
            //2.create preparedStatement
            SelectStatement selectStatement = configuration.getMapper(method.getDeclaringClass())
                    .getMethod2sql().get(method.getName());
            String sql = selectStatement.getSqlStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,(Long) args[0]);
            //3.execute sql
            ResultSet resultSet = preparedStatement.executeQuery();
            //4.parse resultSet
            List<Object> resList = parseResultSet(resultSet, Class.forName(selectStatement.getResultType()));
            //5.close connection
            transaction.close(connection);
            return resList.get(0);
        }
    }

    // 通过反射获取结果集中每个结果的属性并设置值
    private List<Object> parseResultSet(ResultSet resultSet, Class cls) throws Exception {
        ArrayList<Object> resList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colCnt = metaData.getColumnCount();
        while (resultSet.next()){
            Object res = cls.newInstance();
            for (int i = 1; i <= colCnt; i++) {
                Object resValue = resultSet.getObject(i);
                String colName = metaData.getColumnName(i);
                String setMethodName = "set" + colName.substring(0,1).toUpperCase(Locale.ENGLISH) + colName.substring(1);
                Method setMethod = cls.getDeclaredMethod(setMethodName,resValue.getClass());
                setMethod.invoke(res,resValue);
            }
            resList.add(res);
        }
        return resList;
    }
}
