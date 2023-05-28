package com.danzz.executor;

import com.danzz.config.Configuration;
import com.danzz.config.Mapper;
import com.danzz.executor.result.ResultHandler;
import com.danzz.executor.statement.PreparedStatementHandler;
import com.danzz.executor.statement.StatementHandler;
import com.danzz.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction,StatementHandler statementHandler,ResultHandler resultHandler) {
        super(configuration,transaction,statementHandler,resultHandler);
    }

    @Override
    public <T> List<T> doQuery(Object[] args, ResultHandler resultHandler) throws Exception {
        Connection connection = null;
        try{
            PreparedStatementHandler statementHandler = (PreparedStatementHandler) super.getStatementHandler();
            // 获取connection
            connection = getTransaction().getConnection();
            // 获取statement
            Statement statement = statementHandler.prepared(connection);
            // 预处理参数
            statementHandler.parametrize(statement,args);
            // 反序列化结果集
            List<Object> result = statementHandler.query(statement, resultHandler);
            return (List<T>) result;
        }catch (Exception e){
            log.error("query exeception:{}",e.getMessage());
            throw e;
        }finally {
            connection.close();
        }
    }
}
