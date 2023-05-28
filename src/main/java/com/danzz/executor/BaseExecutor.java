package com.danzz.executor;

import com.danzz.config.Configuration;
import com.danzz.config.Mapper;
import com.danzz.executor.result.ResultHandler;
import com.danzz.executor.statement.StatementHandler;
import com.danzz.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.SQLException;
import java.util.List;

@Data
@AllArgsConstructor
public abstract class BaseExecutor implements Executor {

    private Configuration configuration;
    private Transaction transaction;
    private StatementHandler statementHandler;

    private ResultHandler resultHandler;
    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction,StatementHandler statementHandler,ResultHandler resultHandler) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.statementHandler = statementHandler;
        this.resultHandler = resultHandler;
    }

    @Override
    public List query(Object[] args) throws Exception {
        if (closed){
            throw new SQLException("statement already closed");
        }
        return doQuery(args,resultHandler);
    }

    public abstract <T> List<T> doQuery(Object[] args,ResultHandler resultHandler) throws Exception;

    @Override
    public void commit() throws SQLException {
        if (closed){
            throw new SQLException("statement already closed");
        }
        transaction.commit();
    }

    @Override
    public void rollback() throws SQLException {
        if (closed){
            throw new SQLException("statement already closed");
        }
        transaction.rollback();
    }

    @Override
    public void close() throws SQLException {
        if (closed){
            throw new SQLException("statement already closed");
        }
        closed = true;
        transaction = null;
    }
}
