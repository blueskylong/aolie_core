package com.ranranx.aolie.exceptions;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/31 17:07
 * @Version V0.0.1
 **/
public class DataInvalidException extends InvalidException {
    public DataInvalidException(String errorInfo) {
        super("数据存在错误," + errorInfo);
    }
}
