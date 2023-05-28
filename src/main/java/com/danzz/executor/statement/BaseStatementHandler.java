package com.danzz.executor.statement;

import com.danzz.config.Configuration;
import com.danzz.config.Mapper;
import com.danzz.config.SelectStatement;
import com.danzz.executor.result.ResultHandler;
import com.sun.org.apache.bcel.internal.generic.Select;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Data
@AllArgsConstructor
public abstract  class BaseStatementHandler implements StatementHandler{
    private Mapper mapper;

    private SelectStatement selectStatement;

    @Override
    public Statement prepared(Connection connection) throws SQLException {
        Statement statement = initStatement(connection);
        // TODO默认配置
        statement.setQueryTimeout(5000);
        statement.setFetchSize(10000);
        return statement;
    }

    public abstract Statement initStatement(Connection connection) throws SQLException;
}
