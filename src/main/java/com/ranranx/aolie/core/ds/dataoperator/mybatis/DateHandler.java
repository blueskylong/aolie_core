package com.ranranx.aolie.core.ds.dataoperator.mybatis;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/4 0004 19:58
 */


import com.ranranx.aolie.core.common.Constants;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Date;

@MappedTypes({Date.class,Timestamp.class})
@MappedJdbcTypes(value = {JdbcType.TIMESTAMP, JdbcType.DATE,JdbcType.VARCHAR})
public class DateHandler extends BaseTypeHandler<DateHandler.MyDate> {
    public DateHandler() {
        System.out.println("xxx");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MyDate parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    public DateHandler.MyDate getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnName);
        if (sqlTimestamp != null) {
            return new MyDate(sqlTimestamp.getTime());
        }
        return null;
    }

    @Override
    public DateHandler.MyDate getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            return new MyDate(sqlTimestamp.getTime());
        }
        return null;
    }

    @Override
    public DateHandler.MyDate getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            return new MyDate(sqlTimestamp.getTime());
        }
        return null;
    }

    public static class MyDate extends Date {
        public MyDate(long time) {
            super(time);
        }

        public MyDate(Timestamp timestamp) {
            super(timestamp.getTime());
        }

        @Override
        public String toString() {
            return Constants.DATE_FORMAT.format(this);
        }
    }
}



