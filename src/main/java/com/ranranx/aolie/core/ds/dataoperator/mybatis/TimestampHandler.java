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

@MappedTypes({Date.class, Timestamp.class})
@MappedJdbcTypes(value = {JdbcType.TIMESTAMP, JdbcType.DATE})
public class TimestampHandler extends BaseTypeHandler<TimestampHandler.MyTimestamp> {
    public TimestampHandler() {
        System.out.println("xxx");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MyTimestamp parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    public MyTimestamp getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnName);
        if (sqlTimestamp != null) {
            return new MyTimestamp(sqlTimestamp.getTime());
        }
        return null;
    }

    @Override
    public MyTimestamp getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            return new MyTimestamp(sqlTimestamp.getTime());
        }
        return null;
    }

    @Override
    public MyTimestamp getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            return new MyTimestamp(sqlTimestamp.getTime());
        }
        return null;
    }

    public static class MyTimestamp extends Timestamp {
        public MyTimestamp(long time) {
            super(time);
        }

        public MyTimestamp(Timestamp timestamp) {
            super(timestamp.getTime());
        }

        @Override
        public String toString() {
            return Constants.DATE_FORMAT.format(this);
        }
    }
}



