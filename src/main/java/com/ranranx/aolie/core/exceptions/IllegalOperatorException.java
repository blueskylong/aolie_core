package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/12/10 10:27
 * @version V0.0.1
 **/
public class IllegalOperatorException extends BaseException {

    public IllegalOperatorException(String errorInfo) {
        super("不合法的操作:" + errorInfo);
    }
}
