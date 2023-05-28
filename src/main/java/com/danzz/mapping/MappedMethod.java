package com.danzz.mapping;

import com.danzz.sqlsession.SqlSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.SQLException;

@Data
@AllArgsConstructor
public class MappedMethod {

    String id;

    SqlCommandTypeEnum commandType;

    public Object execute(SqlSession sqlSession,Object[] args) throws Exception {
        switch (this.commandType){
            case DELETE:
                break;
            case INSERT:
                break;
            case UPDATE:
                break;
            case SELECT:
                return sqlSession.selectOne(this.id,args);
        }
        return null;
    }
}
