package com.danzz.executor.statement;

import com.danzz.config.Mapper;
import com.danzz.executor.result.ResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    Statement prepared(Connection connection) throws SQLException;

    void parametrize(Statement statement,Object[] args) throws SQLException;

    <T> List<T> query(Statement statement, ResultHandler resultHandler) throws SQLException, ClassNotFoundException, Exception;
}
