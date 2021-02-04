package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/6 14:32
 * @version V0.0.1
 **/
public class InvalidParamException extends InvalidException {
    public InvalidParamException(String errorInfo) {
        super("不正确的参数," + errorInfo);
    }

}
