package com.danzz.executor;

import com.danzz.config.Mapper;
import com.danzz.executor.result.ResultHandler;
import com.danzz.executor.statement.StatementHandler;

import java.sql.SQLException;
import java.util.List;

public interface Executor<T> {

    List<T> query(Object[] args) throws Exception;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
