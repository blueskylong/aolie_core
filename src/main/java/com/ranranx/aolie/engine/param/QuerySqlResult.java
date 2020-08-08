package com.ranranx.aolie.engine.param;

import com.ranranx.aolie.ds.definition.FieldOrder;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 17:18
 * @Version V0.0.1
 **/
public class QuerySqlResult {
    private String sqlExpress;
    private Object[] params;
    private List<FieldOrder> lstOrder;
}
