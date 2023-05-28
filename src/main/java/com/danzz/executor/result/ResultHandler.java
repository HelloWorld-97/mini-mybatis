package com.danzz.executor.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultHandler {

    <T> List<T> resultSet2Obj(ResultSet resultSet,Class<T> clazz) throws SQLException, Exception;
}
