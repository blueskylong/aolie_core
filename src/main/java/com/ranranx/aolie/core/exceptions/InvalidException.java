package com.ranranx.aolie.core.exceptions;

/**
 * @author xxl
 *
 * @date 2020/8/6 14:32
 * @version V0.0.1
 **/
public class InvalidException extends BaseException {
    public InvalidException(String errorInfo) {
        super("信息验证不通过," + errorInfo);
    }

}
