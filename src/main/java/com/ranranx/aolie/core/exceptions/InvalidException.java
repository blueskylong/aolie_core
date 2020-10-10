package com.ranranx.aolie.core.exceptions;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/6 14:32
 * @Version V0.0.1
 **/
public class InvalidException extends BaseException {
    public InvalidException(String errorInfo) {
        super("Invalid exception," + errorInfo);
    }

}
