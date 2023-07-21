package com.danzz.executor.statement;

import com.danzz.config.Mapper;
import com.danzz.config.SelectStatement;
import com.danzz.executor.result.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Mapper mapper,SelectStatement selectStatement){
        super(mapper,selectStatement);
    }

    @Override
    public Statement initStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(getSelectStatement().getSqlStatement());
    }

    @Override
    public void parametrize(Statement statement, Object[] args) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.setLong(1,(Long)args[0]);
    }

    @Override
    public <T> List<T> query(Statement statement, ResultHandler resultHandler) throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        return (List<T>) resultHandler.resultSet2Obj(preparedStatement.executeQuery(),Class.forName(getSelectStatement().getResultType()));
    }
}
