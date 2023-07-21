package com.danzz.executor.result;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SimpleResultHandler implements ResultHandler{
    @Override
    public <T> List<T> resultSet2Obj(ResultSet resultSet, Class<T> clazz) throws Exception {
        ArrayList<Object> resList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colCnt = metaData.getColumnCount();
        while (resultSet.next()){
            Object res = clazz.newInstance();
            for (int i = 1; i <= colCnt; i++) {
                Object resValue = resultSet.getObject(i);
                String colName = metaData.getColumnName(i);
                String setMethodName = "set" + colName.substring(0,1).toUpperCase(Locale.ENGLISH) + colName.substring(1);
                Method setMethod = null;
                if (resValue instanceof Timestamp){
                    setMethod = clazz.getDeclaredMethod(setMethodName, Date.class);
                }else {
                    setMethod = clazz.getDeclaredMethod(setMethodName,resValue.getClass());
                }
                setMethod.invoke(res,resValue);
            }
            resList.add(res);
        }
        return (List<T>) resList;
    }
}
