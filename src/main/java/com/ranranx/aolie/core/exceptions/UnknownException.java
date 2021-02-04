package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/10 9:48
 * @version V0.0.1
 **/
public class UnknownException extends BaseException {

    public UnknownException(String errorInfo) {
        super("未知错误:" + errorInfo);

    }
}
