package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.handler.param.QueryParam;

/**
 * 不存在子查询
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 20:52
 **/
public class NotExists extends Exists {
    public NotExists(QueryParam value) {
        super(value);
    }


    @Override
    protected String getKeyWord() {
        return "not exists";
    }

}
