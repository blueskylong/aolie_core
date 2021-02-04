package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/31 17:07
 * @version V0.0.1
 **/
public class DataInvalidException extends InvalidException {
    public DataInvalidException(String errorInfo) {
        super("数据存在错误," + errorInfo);
    }
}
