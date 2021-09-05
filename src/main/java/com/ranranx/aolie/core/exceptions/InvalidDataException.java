package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/10 9:48
 **/
public class InvalidDataException extends BaseException {

    public InvalidDataException(String errorInfo) {
        super("数据不合法:" + errorInfo);

    }
}
