package com.bluemobi.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gaoll on 2015/7/23.
 */
public class IntegerIfNull implements TypeHandler<Integer> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Integer aInteger, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public Integer getResult(ResultSet resultSet, String columnName) throws SQLException {
        return (resultSet.getString(columnName) == null) ? 0 : resultSet.getInt(columnName);
    }

    @Override
    public Integer getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Integer getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
