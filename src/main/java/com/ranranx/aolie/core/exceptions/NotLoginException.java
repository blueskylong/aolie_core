package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/11 0011 11:09
 **/
public class NotLoginException extends BaseException {

    public NotLoginException(String errorInfo) {
        super("用户没有登录");

    }
}